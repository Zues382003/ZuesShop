package com.zuesshopbackend.category;

import com.zuesshop.entity.Category;
import com.zuesshopbackend.category.Repository.CategoryReposity;
import com.zuesshopbackend.category.Service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)// cho phép sử dụng Mockito để tạo ra mock object cho CategoryReposity và @InjectMocks sẽ tiêm nạp mock object vào CategoryService
@ExtendWith(SpringExtension.class)//cho phép JUnit tích hợp với Spring Framework trong quá trình chạy unit test. Nó tạo ra một môi trường Spring để chạy unit test và khởi tạo các bean và dependency injection (DI) của Spring trong test context.
public class CategoryServiceTests {

    @MockBean// Mock object được tạo ra để giả lập (mock) đối tượng
    private CategoryReposity repo;

    @InjectMocks//chỉ định rằng các mock object được khai báo với @MockBean sẽ được tiêm nạp vào CategoryService để sử dụng trong quá trình chạy các unit test.
    private CategoryService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate(){
        Integer id = null;
        String name = "Zues";
        String alias = "Computers";

        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.checkUniqueNameAndAlias(id,name,alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }
}
