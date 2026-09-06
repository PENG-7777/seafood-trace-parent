package com.peng.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.admin.entity.Province;
import com.peng.admin.mapper.ProvinceMapper;
import com.peng.admin.service.ProvinceService;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements ProvinceService {}