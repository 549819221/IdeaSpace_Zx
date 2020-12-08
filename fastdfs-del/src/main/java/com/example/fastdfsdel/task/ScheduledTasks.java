package com.example.fastdfsdel.task;

import com.example.fastdfsdel.dao.PackageSerialDao;
import com.example.fastdfsdel.service.BasisService;
import com.example.fastdfsdel.util.DateUtil;
import com.example.fastdfsdel.util.FTPUtil;
import com.example.fastdfsdel.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class ScheduledTasks {
    Logger logger = LoggerFactory.getLogger( ScheduledTasks.class);




}
