package com.zuesshopbackend.product.Controller;

import com.zuesshop.entity.Product;
import com.zuesshop.entity.ProductImage;
import com.zuesshopbackend.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ProductSaveHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
    static void deleteExtraImageWereRemoveOnForm(Product product) {
        String extraImageDir = "./product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String fileName = file.toFile().getName();

                if (!product.containsImageName(fileName)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Delete extra images: " + fileName);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + fileName);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory: " + dirPath);
        }
    }

    static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIDs.length; i++) {
            Integer id = Integer.parseInt(imageIDs[i]);
            String name = imageNames[i];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    static void saveUploadImages(MultipartFile mainMultipartFile, MultipartFile[] extraMultipartFile, Product saveProduct) throws IOException {
        if (!mainMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            String uploadDir = "./product-images/" + saveProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainMultipartFile);
        }

        if (extraMultipartFile.length > 0) {
            String uploadDir = "./product-images/" + saveProduct.getId() + "/extras";
            for (MultipartFile multipartFile : extraMultipartFile) {
                if (multipartFile.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    static void setMainImageName(MultipartFile mainMultipartFile, Product product) throws IOException {
        if (!mainMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    static void setNewExtraImageNames(MultipartFile[] extraMultipartFile, Product product) throws IOException {
        if (extraMultipartFile.length > 0) {
            for (MultipartFile multipartFile : extraMultipartFile) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    if (!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

    static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValue, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int i = 0; i < detailNames.length; i++) {
            String name = detailNames[i];
            String value = detailValue[i];
            Integer id = Integer.parseInt(detailIDs[i]);

            boolean isCreatingNewDetail = (id == 0);// New Detail

            if (!isCreatingNewDetail) {
                product.addDetail(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }
}
