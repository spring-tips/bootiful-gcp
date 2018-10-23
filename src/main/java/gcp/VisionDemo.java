package gcp;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.Collections;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@Log4j2
@Configuration
public class VisionDemo {

	private final Feature labelDetection = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
	private final Feature textDetection = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
	private final Resource cat;
	private final ImageAnnotatorClient client;

	VisionDemo(@Value("file://${user.home}/Desktop/cat.jpg") Resource resource,
												CredentialsProvider cp) throws IOException {
		this.cat = resource;
		this.client = this.buildImageAnnotatorClient(cp);
	}

	private String process(byte[] data) {
		ByteString imgBytes = ByteString.copyFrom(data);
		Image img = Image.newBuilder().setContent(imgBytes).build();
		AnnotateImageRequest request = AnnotateImageRequest
			.newBuilder()
			.addFeatures(this.labelDetection)
			.addFeatures(this.textDetection)
			.setImage(img)
			.build();
		BatchAnnotateImagesResponse responses = this.client
			.batchAnnotateImages(Collections.singletonList(request));
		AnnotateImageResponse reply = responses.getResponses(0);
		return reply.toString();
	}

	private ImageAnnotatorClient buildImageAnnotatorClient(CredentialsProvider cp) throws IOException {
		ImageAnnotatorSettings settings = ImageAnnotatorSettings
			.newBuilder()
			.setCredentialsProvider(cp)
			.build();
		return ImageAnnotatorClient.create(settings);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void vision() throws Exception {
		byte[] data = FileCopyUtils.copyToByteArray(this.cat.getInputStream());
		log.info("result of cat-analysis: " + this.process(data));
	}
}
