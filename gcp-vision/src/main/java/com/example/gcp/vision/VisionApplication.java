package com.example.gcp.vision;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.autoconfigure.core.GcpProperties;
import org.springframework.cloud.gcp.core.GcpScope;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// curl -F "image=@$HOME/Pictures/soup.jpg" http://localhost:8080/analyze
@SpringBootApplication
public class VisionApplication {

		// <1>
		@Bean
		ImageAnnotatorClient imageAnnotatorClient(
			CredentialsProvider credentialsProvider) throws IOException {
				ImageAnnotatorSettings settings = ImageAnnotatorSettings
					.newBuilder()
					.setCredentialsProvider(credentialsProvider)
					.build();
				return ImageAnnotatorClient.create(settings);
		}

		@Slf4j
		@RestController
		public static class ImageAnalyzerRestController {

				private final ImageAnnotatorClient client;

				private final Feature feature = Feature
					.newBuilder()
					.setType(Feature.Type.LABEL_DETECTION) // <2>
					.build();

				ImageAnalyzerRestController(ImageAnnotatorClient client) {
						this.client = client;
				}

				@PostMapping("/analyze")
				String analyze(@RequestParam MultipartFile image) throws IOException {
						// <3>
						byte[] data = image.getBytes();
						ByteString imgBytes = ByteString.copyFrom(data);
						Image img = Image.newBuilder().setContent(imgBytes).build();
						AnnotateImageRequest request = AnnotateImageRequest
							.newBuilder()
							.addFeatures(this.feature)
							.setImage(img)
							.build();
						BatchAnnotateImagesResponse responses = this.client
							.batchAnnotateImages(Collections.singletonList(request));
						AnnotateImageResponse reply = responses.getResponses(0);
						return reply.toString();
				}
		}

		@Bean
		CredentialsProvider googleCredentials(GcpProperties gcpProperties) throws Exception {
				return new SimpleDefaultCredentialsProvider(gcpProperties);
		}


		public static void main(String args[]) {
				SpringApplication.run(VisionApplication.class, args);
		}
}

class SimpleDefaultCredentialsProvider implements CredentialsProvider {

		private static final String DEFAULT_SCOPES_PLACEHOLDER = "DEFAULT_SCOPES";

		private static final List<String> CREDENTIALS_SCOPES_LIST = Collections.unmodifiableList(
			Arrays.stream(GcpScope.values())
				.map(GcpScope::getUrl)
				.collect(Collectors.toList()));

		private final CredentialsProvider wrappedCredentialsProvider;

		SimpleDefaultCredentialsProvider(GcpProperties credentialsSupplier) throws Exception {

				List<String> scopes = resolveScopes(credentialsSupplier.getCredentials().getScopes());
				String encodedKey = credentialsSupplier.getCredentials().getEncodedKey();

				GoogleCredentials scoped = GoogleCredentials
					.fromStream(new ByteArrayInputStream(encodedKey.getBytes()))
					.createScoped(scopes);

				this.wrappedCredentialsProvider = FixedCredentialsProvider.create(scoped);
		}

		@Override
		public Credentials getCredentials() throws IOException {
				return this.wrappedCredentialsProvider.getCredentials();
		}

		private List<String> resolveScopes(List<String> scopes) {
				if (!ObjectUtils.isEmpty(scopes)) {
						Set<String> resolvedScopes = new HashSet<>();
						scopes.forEach(scope -> {
								if (DEFAULT_SCOPES_PLACEHOLDER.equals(scope)) {
										resolvedScopes.addAll(CREDENTIALS_SCOPES_LIST);
								}
								else {
										resolvedScopes.add(scope);
								}
						});
						return Collections.unmodifiableList(new ArrayList<>(resolvedScopes));
				}
				return CREDENTIALS_SCOPES_LIST;
		}
}