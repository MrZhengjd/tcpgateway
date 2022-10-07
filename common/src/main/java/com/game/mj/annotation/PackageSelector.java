package com.game.mj.annotation;

import com.game.mj.model.GameMessage;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zheng
 */
public class PackageSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String ,Object> attributes = annotationMetadata.getAnnotationAttributes(EnablePackage.class.getName());
        String[] basePackages = (String[]) attributes.get("basePackages");
        if (basePackages == null || basePackages.length == 0){
            String basePackage = null;
            try {
                basePackage = Class.forName(annotationMetadata.getClassName()).getPackage().getName();
            }catch (Exception e){
                e.printStackTrace();
            }
            basePackages = new String[]{basePackage};
        }
        ClassPathScanningCandidateComponentProvider scanne = new ClassPathScanningCandidateComponentProvider(false);
        TypeFilter gameMessageFilter = new AssignableTypeFilter(GameMessage.class);
//        TypeFilter dispatche = new AssignableTypeFilter(EventDispatchService.class);
        scanne.addIncludeFilter(gameMessageFilter);
//        scanne.addIncludeFilter(dispatche);
        Set<String> classes = new HashSet<>();
        for (String basePackage :basePackages){
            scanne.findCandidateComponents(basePackage).forEach(beanDefinition -> {
                classes.add(beanDefinition.getBeanClassName());
            });
        }
        return classes.toArray(new String[classes.size()]);
    }
}
