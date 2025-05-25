package com.example.memecommerceback.domain.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductResponseDto {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(description = "Response payload after registering a single product")
  public static class RegisterOneDto {
    @Schema(description = "Identifier of the created product", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "Name of the product", example = "Vintage Lamp")
    private String name;

    @Schema(description = "Description of the product", example = "A beautiful antique lamp")
    private String description;

    @Schema(description = "Price in cents", example = "15000")
    private Long price;

    @Schema(description = "Name of the product owner", example = "John Doe")
    private String ownerName;

    @Schema(description = "List of product images", example = "[]")
    private List<ImageResponseDto> imageResponseDtoList;

    // private List<CategoryResponseDto.ReadOneDto> categoryList;
    // private List<HashtagResponseDto.ReadOneDto> hashtagList;

    @Schema(description = "Available stock count", example = "100")
    private Long stock;

    @Schema(description = "Registered company name", example = "Acme Corp")
    private String registeredCompanyName;

    @Schema(description = "Current status of the product", example = "AVAILABLE")
    private ProductStatus status;

    @Schema(description = "Creation timestamp", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(description = "Response payload after updating a product")
  public static class UpdateOneDto {
    @Schema(description = "Identifier of the product", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "Name of the product", example = "Vintage Lamp")
    private String name;

    @Schema(description = "Description of the product", example = "A beautiful antique lamp")
    private String description;

    @Schema(description = "Price in cents", example = "15000")
    private Long price;

    @Schema(description = "Name of the product owner", example = "John Doe")
    private String ownerName;

    @Schema(description = "List of product images", example = "[]")
    private List<ImageResponseDto> imageResponseDtoList;

    // private List<CategoryResponseDto.ReadOneDto> categoryList;
    // private List<HashtagResponseDto.ReadOneDto> hashtagList;

    @Schema(description = "Available stock count", example = "100")
    private Long stock;

    @Schema(description = "Registered company name", example = "Acme Corp")
    private String registeredCompanyName;

    @Schema(description = "Current status of the product", example = "AVAILABLE")
    private ProductStatus status;

    @Schema(description = "Creation timestamp", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(description = "Response payload after updating product status")
  public static class UpdateOneStatusDto {
    @Schema(description = "Identifier of the product", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "Name of the product", example = "Vintage Lamp")
    private String name;

    @Schema(description = "Updated status of the product", example = "AVAILABLE")
    private ProductStatus status;

    @Schema(description = "Creation timestamp", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Last modified timestamp", example = "2025-05-16T11:20:45")
    private LocalDateTime modifiedAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(description = "Response payload for reading a single product")
  public static class ReadOneDto {
    @Schema(description = "Identifier of the product", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "Name of the product", example = "Vintage Lamp")
    private String name;

    @Schema(description = "Description of the product", example = "A beautiful antique lamp")
    private String description;

    @Schema(description = "Price in cents", example = "15000")
    private Long price;

    @Schema(description = "Name of the product owner", example = "John Doe")
    private String ownerName;

    @Schema(description = "List of product images", example = "[]")
    private List<ImageResponseDto> imageResponseDtoList;

    // private List<CategoryResponseDto.ReadOneDto> categoryList;
    // private List<HashtagResponseDto.ReadOneDto> hashtagList;

    @Schema(description = "Available stock count", example = "100")
    private Long stock;

    @Schema(description = "Number of likes", example = "42")
    private Integer likeCount;

    @Schema(description = "Number of views", example = "100")
    private Integer viewCount;

    @Schema(description = "Current status of the product", example = "AVAILABLE")
    private ProductStatus status;

    @Schema(description = "Creation timestamp", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;
  }
}