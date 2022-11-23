package com.madoor.resource_toolkit.util;

import com.madoor.resource_toolkit.projection.Concept2ResourcePro;
import com.madoor.resource_toolkit.projection.Resource2ConceptPro;
import com.madoor.resource_toolkit.repository.ConceptRepository;
import com.madoor.resource_toolkit.repository.ResourceRepository;
import com.madoor.resource_toolkit.vo.ConceptVO;
import com.madoor.resource_toolkit.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class SimilarityUtil {
    private final ConceptRepository conceptRepository;
    private final ResourceRepository resourceRepository;
    public void calculate(List<Keyword> keywords){
        HashMap<String, Integer> conceptFreqMap = new HashMap<>();
        keywords.forEach(keyword -> {
            conceptFreqMap.put(keyword.getName(),keyword.getFreq());
        });
        double powA = 0;
        HashSet<Long> idSet = new HashSet<>();
        for (Keyword keyword : keywords) {
            String conceptName = keyword.getName();
            powA += Math.pow((double) keyword.getFreq(),2);
            Concept2ResourcePro concept = conceptRepository.findConceptEntityByName(conceptName);
            if (concept!=null){
                    for (Concept2ResourcePro.KeywordRel keywordRel : concept.getResourceList()) {
                        Long id = keywordRel.getResource().getId();
                        idSet.add(id);
                    }
            }
        }
        //计算余弦相似度
        for (Long id : idSet) {
            System.out.println("资源id:"+id);
            System.out.println("相似度:"+cosineSimilarity(id,powA,conceptFreqMap));
        }
        System.out.println("相似资源总数"+idSet.size());
    }
    private Double cosineSimilarity(Long id,double powA,HashMap<String, Integer> conceptFreqMap){
        double powB = 0;
        //分子
        double numerator = 0;
        Resource2ConceptPro resourceEntity = resourceRepository.findFirstById(id);
        List<Resource2ConceptPro.KeywordRel> conceptListB = resourceEntity.getConceptList();
        for (Resource2ConceptPro.KeywordRel keywordRel : conceptListB) {
            String conceptName = keywordRel.getConcept().getName();
            Integer frequency = keywordRel.getFrequency();
            if (conceptFreqMap.containsKey(conceptName)){
                numerator += conceptFreqMap.get(conceptName)*frequency;
                powB += Math.pow(frequency,2);
            }
        }
        //分母
        double denominator = Math.sqrt(powA) + Math.sqrt(powB);
        return numerator/denominator;
    }
}

