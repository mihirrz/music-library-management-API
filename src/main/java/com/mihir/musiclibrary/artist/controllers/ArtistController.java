package com.mihir.musiclibrary.artist.controllers;

import com.mihir.musiclibrary.Response.ApiResponse;
import com.mihir.musiclibrary.Response.ErrorDetails;
import com.mihir.musiclibrary.artist.dto.ArtistDTO;
import com.mihir.musiclibrary.artist.entity.ArtistEntity;
import com.mihir.musiclibrary.artist.services.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping("/artists/add-artist")
    public ResponseEntity<ApiResponse<?>> addArtist(@Valid @RequestBody ArtistDTO artistDTO, BindingResult bindingResult)
    {

        if (bindingResult.hasErrors()) {
            List<ErrorDetails> errorDetails = bindingResult.getFieldErrors().stream()
                    .map(error -> new ErrorDetails(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, null, "Validation failed", errorDetails));
        }

        ArtistEntity addedArtist = artistService.addArtist(artistDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, null, "Artist added successfully.", null));
    }

    @GetMapping("/artists")
    public ResponseEntity<ApiResponse<?>> getAllArtist()
    {
        List<ArtistEntity> fetchedArtists = artistService.fetchArtists();
        if(fetchedArtists.isEmpty())
            return ResponseEntity.ok(new ApiResponse<>(200, null, "No Artists found", null));
        return ResponseEntity.ok(new ApiResponse<>(200, fetchedArtists, "Artists fetched successfully", null));
    }


}
