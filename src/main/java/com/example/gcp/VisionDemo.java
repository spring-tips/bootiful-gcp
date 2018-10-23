package com.example.gcp;

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


	private final ImageAnnotatorClient client;
	private final Resource cat;

	VisionDemo(@Value("file://${user.home}/Desktop/cat.jpg") Resource cat,
												CredentialsProvider cp) throws IOException {
		this.cat = cat;
		this.client = this.buildImageAnnotatorClient(cp);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void vision() throws Exception {

		Feature labelsFeature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
		Feature documentsFeature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
		final byte[] data = FileCopyUtils.copyToByteArray(this.cat.getInputStream());
		Image img = Image.newBuilder().setContent(ByteString.copyFrom(data)).build();
		AnnotateImageRequest air = AnnotateImageRequest
			.newBuilder()
			.addFeatures(labelsFeature)
			.addFeatures(documentsFeature)
			.setImage(img)
			.build();

		BatchAnnotateImagesResponse responses = this.client
			.batchAnnotateImages(Collections.singletonList(air));
		AnnotateImageResponse response = responses.getResponses(0);
		String analysis = response.toString();
		log.info("vision analysis: " + analysis);

	}

	private ImageAnnotatorClient buildImageAnnotatorClient(CredentialsProvider cp) throws IOException {

		ImageAnnotatorSettings imageAnnotatorSettings =
			ImageAnnotatorSettings
				.newBuilder()
				.setCredentialsProvider(cp)
				.build();
		return ImageAnnotatorClient.create(imageAnnotatorSettings);
	}
}
