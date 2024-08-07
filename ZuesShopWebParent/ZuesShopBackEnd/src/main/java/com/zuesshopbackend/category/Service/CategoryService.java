package com.zuesshopbackend.category.Service;

import com.zuesshop.entity.Category;
import com.zuesshop.exception.CategoryNotFoundException;
import com.zuesshopbackend.category.CategoryPageInfo;
import com.zuesshopbackend.category.Repository.CategoryReposity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.*;

@Service
@Transactional // update cho repository
public class CategoryService {
    @Autowired
    CategoryReposity repo;

    public static final int CATEGORY_PER_PAGE = 1;

    public long totalCategories(){
        return repo.count();
    }

    public List<Category> listByPage(int pageNumber, CategoryPageInfo categoryPageInfo,String sortField ,String sortDirection,String keyword) throws CategoryNotFoundException {
        Sort sort = Sort.by(sortField);

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        Page<Category> pageCategories = null;
        if(keyword != null){
            Pageable pageable = PageRequest.of(pageNumber - 1, 8, sort);
            pageCategories = repo.search(keyword,pageable);
            categoryPageInfo.setTotalElements(pageCategories.getTotalElements());
        }else{
            Pageable pageable = PageRequest.of(pageNumber - 1, CATEGORY_PER_PAGE, sort);
            pageCategories = repo.listRootCategory(pageable);

        }
        categoryPageInfo.setTotalPages(pageCategories.getTotalPages());
        List<Category> rootCategories = pageCategories.getContent();

        if(keyword != null){
            List<Category> searchResult = pageCategories.getContent();
            for(Category category : searchResult){
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchResult;
        }else{
            List<Category> listAllCategory = new ArrayList<>();

            for (Category category :rootCategories){
                listAllCategory.add(Category.copyFull(category));
                listChildrenCategory(listAllCategory,category,0,sortDirection);
            }
            return listAllCategory;
        }

    }

    private void listChildrenCategory(List<Category> listAllCategory, Category parent, int subLevel, String sortDir){
        int newSubLevel = subLevel + 1;
        Set<Category> childrenCategory = sortSubCategories(parent.getChildren(),sortDir);

        for(Category subCategory : childrenCategory){
            String name = "";

            for(int i = 0 ; i < newSubLevel; i++ ){
                name += " * ";
            }
            listAllCategory.add(Category.copyFull(subCategory,"(" + name + ")" + "   "+ subCategory.getName()));
            listChildrenCategory(listAllCategory,subCategory,newSubLevel,sortDir);
        }

    }

    public Category save(Category category){
        Category parent = category.getParent();
        if(parent != null){
            String allParentIDs = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIDs += String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIDs);
        }
        return repo.save(category);
    }

    public List<Category> listCategoryUsedInForm(){
        List<Category> listCategoryUsedInForm = new ArrayList<>();

        Iterable<Category> listCategoryInDB = repo.listRootCategory(Sort.by("name").ascending());

        for(Category category : listCategoryInDB){
            if(category.getParent() == null){
                listCategoryUsedInForm.add(Category.copyIdAndName(category));

                listChildren(listCategoryUsedInForm,category,0);
            }
        }

        return listCategoryUsedInForm;
    }

    private void listChildren(List<Category> listCategoryUsedInForm, Category parent, int subLevel){
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for(Category subCategory : children){
            String name = "";
            for(int i = 0; i< newSubLevel; i++)
            {
                name += "*";
            }

            System.out.println(subCategory.getName());

            listCategoryUsedInForm.add(Category.copyIdAndName(subCategory.getId(),"(" + name + ")" + subCategory.getName()));
            listChildren(listCategoryUsedInForm,subCategory,newSubLevel);
        }
    }

    public Category getByID(int id) throws CategoryNotFoundException {
        try{
            return repo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("Could not find category with ID " + id);
        }
    }

    public String checkUniqueNameAndAlias(Integer id, String name, String alias){
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = repo.findByName(name);

        if(isCreatingNew){
            if (categoryByName != null) {
                return "DuplicateName";
            }
            Category categoryByAlias = repo.findByAlias(alias);
            if(categoryByAlias != null){
                return "DuplicateAlias";
            }

        }
        else{
            if(categoryByName != null && categoryByName.getId() != id){
                return "DuplicateName";
            }
            Category categoryByAlias = repo.findByAlias(alias);
            if(categoryByAlias != null && categoryByAlias.getId() != id){
                return "DuplicateAlias";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children){
        return sortSubCategories(children,"asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children,String sortDir){
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category category1, Category category2) {
                if(sortDir.equals("asc")){
                    return category1.getName().compareTo(category2.getName());
                } else if (sortDir.equals("desc")) {
                    return category2.getName().compareTo(category1.getName());
                }else{
                    try {
                        throw new CategoryNotFoundException("You need choose asc or desc!");
                    } catch (CategoryNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }

    public void updateCategoryEnableStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id,enabled);
    }
    public void delete(Integer id) throws CategoryNotFoundException {
        Long countByID = repo.countById(id);
        if(countByID == 0){
            throw new CategoryNotFoundException("Could not find any category with ID: " + id);
        }
        repo.deleteById(id);
    }
}
