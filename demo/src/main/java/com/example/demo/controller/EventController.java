package com.example.demo.controller;

import com.example.demo.entity.EventInfo;
import com.example.demo.service.EventService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Event)表控制层
 *
 * @author wanghb
 * @since 2020-08-12 17:55:45
 */
@RestController
@RequestMapping("/api/event")
public class EventController {
    /**
     * 服务对象
     */
    @Resource
    private EventService eventService;


    /**
     * @param eventInfo 实体
     * @return 无返回值
     * @description 保存
     * @date 2020-08-12 17:55:45
     * @author wanghb
     * @edit
     */
    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody EventInfo eventInfo) {
        //eventService.save( eventInfo );
    }


    /**
     * @param id 主键id
     * @return 实体对象
     * @description 详情
     * @date 2020-08-12 17:55:45
     * @author wanghb
     * @edit
     */
    @GetMapping("/view")
    @ResponseBody
    public EventInfo view(@RequestParam(name = "id", required = true) String id) {
        //EventInfo eventInfo = eventService.view( id );
        return null;
    }


    /**
     * @param id 主键id
     * @return 实体对象
     * @description 删除
     * @date 2020-08-12 17:55:45
     * @author wanghb
     * @edit
     */
    @GetMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam(name = "id", required = true) String id) {
        //eventService.delete( id );
    }

    /**
     * @param ids 主键id
     * @return 实体对象
     * @description 批量删除
     * @date 2020-07-08 16:03:21
     * @author wanghb
     * @edit
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    public void batchDelete(@RequestBody List<String> ids) {
        //eventService.batchDelete( ids );
    }
}