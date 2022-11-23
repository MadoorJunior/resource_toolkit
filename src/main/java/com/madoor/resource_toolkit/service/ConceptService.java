package com.madoor.resource_toolkit.service;

import com.madoor.resource_toolkit.projection.Concept2ResourcePro;
import com.madoor.resource_toolkit.repository.ConceptRepository;
import com.madoor.resource_toolkit.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConceptService {
    private final ConceptRepository conceptRepository;
    public Response<Concept2ResourcePro> getConceptByName(String name){
        Concept2ResourcePro con = conceptRepository.findConceptEntityByName(name);
        if (con!=null){
            return Response.success(con);
        }
        return Response.fail("实体不存在");
    }
}
