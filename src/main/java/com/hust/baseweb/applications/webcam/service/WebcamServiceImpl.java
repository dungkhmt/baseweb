package com.hust.baseweb.applications.webcam.service;

import com.hust.baseweb.applications.webcam.document.WebcamVideo;
import com.hust.baseweb.applications.webcam.dto.WebcamVideoDto;
import com.hust.baseweb.applications.webcam.repository.WebcamVideoRepository;
import com.hust.baseweb.entity.Content;
import com.hust.baseweb.service.ContentService;
import lombok.Getter;
import okhttp3.Response;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@Getter
public class WebcamServiceImpl implements WebcamService {

    private final ContentService contentService;
    private final WebcamVideoRepository webcamVideoRepository;

    public WebcamServiceImpl(
        ContentService contentService,
        WebcamVideoRepository webcamVideoRepository
    ) {
        this.contentService = contentService;
        this.webcamVideoRepository = webcamVideoRepository;
    }


    @Override
    public WebcamVideoDto upload(MultipartFile multipartFile, String userLoginId) {
        try {
            Date now = new Date();
            String fileName = "WC_" + userLoginId + "_" + now.getTime() + ".webm";
            Content content = contentService.createContent(
                multipartFile.getInputStream(),
                fileName,
                multipartFile.getContentType());
            WebcamVideo webcamVideo = new WebcamVideo(null, userLoginId, now, content.getContentId());
            ModelMapper modelMapper = getModelMapper();
            return modelMapper.map(webcamVideoRepository.save(webcamVideo), WebcamVideoDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Override
    public InputStreamResource get(ObjectId objectId) {
        WebcamVideo webcamVideo = webcamVideoRepository.findById(objectId).orElseThrow(NoSuchElementException::new);
        try {
            Response response = contentService.getContentData(webcamVideo.getContentId().toString());

            return new InputStreamResource(new ByteArrayInputStream(Objects.requireNonNull(response.body()).bytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InputStreamResource(new ByteArrayInputStream(new byte[0]));
    }

    @Override
    public List<WebcamVideoDto> all() {
        ModelMapper modelMapper = getModelMapper();
        return webcamVideoRepository
            .findAll()
            .stream()
            .map(webcamVideo -> modelMapper.map(webcamVideo, WebcamVideoDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<WebcamVideoDto> all(String userLoginId) {
        ModelMapper modelMapper = getModelMapper();
        return webcamVideoRepository
            .findAllByUserLoginId(userLoginId)
            .stream()
            .map(webcamVideo -> modelMapper.map(webcamVideo, WebcamVideoDto.class))
            .collect(Collectors.toList());
    }
}
