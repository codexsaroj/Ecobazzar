package com.ecobazzar.service.impl;

import com.ecobazzar.exception.FileExistsException;
import com.ecobazzar.exception.NotFoundException;
import com.ecobazzar.repository.CategoryRepo;
import com.ecobazzar.repository.ProductRepo;
import com.ecobazzar.service.fileServices.FileServiceImpl;
import com.ecobazzar.service.interf.ProductService;
import com.ecobazzar.dto.ProductDto;
import com.ecobazzar.dto.Response;
import com.ecobazzar.entity.Category;
import com.ecobazzar.entity.Product;
import com.ecobazzar.mapper.EntityDtoMapper;
//import com.phegondev.Phegon.Eccormerce.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;
    private final FileServiceImpl fileServiceImpl;
    //    private final AwsS3Service awsS3Service;
// filepath to store the file
    @Value("${product.image}")
    String filepath;

    @Value("${base.url}")
    String baseUrl;


    /*  @Override
      public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) throws IOException {
          Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
  //        String productImageUrl = awsS3Service.saveImageToS3(image);

          // Define the directory to save the image
          String uploadDir = "src/main/resources/static/images/";

          // Ensure the directory exists
          File dir = new File(uploadDir);
          if (!dir.exists()) {
              dir.mkdirs();
          }

          // Create a unique filename
          String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
          Path filePath = Paths.get(uploadDir + filename);

          // Save the image to the local file system
          Files.write(filePath, image.getBytes());

          String accessDir = "/images/";
          // set the base url for the image to be accessible in frontend
          String baseUrl = "http://localhost:2424";
          // Set the URL to access the image
          String productImageUrl = baseUrl + accessDir + filename;

          Product product = new Product();
          product.setCategory(category);
          product.setPrice(price);
          product.setName(name);
          product.setDescription(description);
          product.setImageUrl(productImageUrl);

          productRepo.save(product);
          return Response.builder()
                  .status(200)
                  .message("Product successfully created")
                  .build();
      }
      */
    @Override
    public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) throws IOException {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));

        if (Files.exists(Paths.get((filepath) + File.separator + image.getOriginalFilename()))) {
            throw new FileExistsException("Image already exists!!");
        }
        // firstly upload the file which returns a filename
        String filename = fileServiceImpl.uploadFile(filepath, image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(filename);

        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

 /*   @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) throws IOException {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));

        Category category = null;
        String productImageUrl = null;

        if (categoryId != null) {
            category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        }
        if (image != null && !image.isEmpty()) {
//            productImageUrl = awsS3Service.saveImageToS3(image);
            // Define the directory to save the image
            String uploadDir = "src/main/resources/static/images/";

            // Ensure the directory exists
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create a unique filename
            String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);

            // Save the image to the local file system
            Files.write(filePath, image.getBytes());
            String accessDir = "/images/";
            // set the base url for the image to be accessible in frontend
            String baseUrl = "http://localhost:2424";
            // Set the URL to access the image
            productImageUrl = baseUrl + accessDir + filename;
        }

        if (category != null) product.setCategory(category);
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        if (productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();

    }*/

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) throws IOException {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));

        Category category = null;


        if (categoryId != null) {
            category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        }
        // if file is null then do nothing
        // else delete existing file in record and add the new file
        String filename = product.getImageUrl();
        if(image != null){
            Files.deleteIfExists(Paths.get(filepath + File.separator + filename));
            // upload the new file
            String urlToImage = fileServiceImpl.uploadFile(filepath, image);
            product.setImageUrl(urlToImage);
        }

        String productImageUrl = product.getImageUrl();

        if (category != null) product.setCategory(category);
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        if (productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();
    }


    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        productRepo.delete(product);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productList)
                .build();

    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            throw new NotFoundException("No Products found for this category");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();

    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);

        if (products.isEmpty()) {
            throw new NotFoundException("No Products Found");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());


        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }
}
