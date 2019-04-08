package com.caicai.activiti_demo;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/activiti")
@RestController
public class ActivitiStart {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    //创建流程

    /**
     *
     * @param apply 申请人
     * @param approve 审批人
     * @param context 申请内容详情
     * @return
     */
    @RequestMapping("/start")
    public String startActiviti(@RequestParam String apply,@RequestParam String approve,@RequestParam String context){
        Map map=new HashMap<String,Object>();
        map.put("apply",apply);//申请人
        map.put("approve",approve);//审批人
        map.put("context",context);
        try{
            runtimeService.startProcessInstanceByKey("leave",map);
        }catch (Exception e){
            return "error";
        }
        return "新建任务成功\r\n任务内容：申请人："+apply+";审批人："+approve+"。";
    }
    //查询某个用户需要处理的任务
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public List<Map> getTasks(@RequestParam String assignee) {
        List<Task> tasks =taskService.createTaskQuery().taskAssignee(assignee).list();
        List<Map> dtos = new ArrayList<Map>();
        for(Task task:tasks){
            System.out.println("#####################################");
            System.out.println("任务ID:"+task.getId());
            System.out.println("任务的办理人:"+task.getAssignee());
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务的创建时间:"+task.getCreateTime());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
            System.out.println("#####################################");
            Map m=new HashMap<String,Object>();
            m.put("任务ID",task.getId());
            m.put("任务的办理人",task.getAssignee());
            m.put("任务名称",task.getName());
            m.put("任务的创建时间",task.getCreateTime());
            m.put("流程实例ID",task.getProcessInstanceId());
            dtos.add(m);
        }
        return dtos;
    }
    //完成某个任务
    @RequestMapping(value="/complete")
    @Transactional
    public String completeTasks(String  comment, @RequestParam String taskId,boolean pass) {
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        if(pass){
            taskVariables.put("pass",pass);
            Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
            //利用任务对象，获取流程实例id
            String processInstancesId=task.getProcessInstanceId();
            try{
                taskService.addComment(taskId,processInstancesId,comment);
                taskService.complete(taskId, taskVariables);
            }catch (Exception e){
                return "审批失败";
            }
            return "审批成功";
        }else{
            return "驳回申请";
        }

    }
    //===============================历史记录==========================================

}
