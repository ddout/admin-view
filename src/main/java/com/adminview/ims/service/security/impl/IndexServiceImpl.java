package com.adminview.ims.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminview.ims.dao.security.IndexMapper;
import com.adminview.ims.service.security.IIndexService;

@Service
public class IndexServiceImpl implements IIndexService {
    @Autowired
    private IndexMapper mapper;

}
