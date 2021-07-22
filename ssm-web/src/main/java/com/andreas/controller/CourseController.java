package com.andreas.controller;

import com.andreas.domain.Course;
import com.andreas.domain.ResponseResult;
import com.andreas.dto.CourseDTO;
import com.andreas.service.CourseService;
import com.andreas.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：CourseController
 */

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    /**
     * @Author: andreaszhou
     * @Description: 根据搜索条件查询课程信息
     * @DateTime: 2021/7/21 8:41
     * @Params: dto
     * @Return
     */
    @RequestMapping("/findCourseByCondition")
    public ResponseResult findCourseByCondition(@RequestBody CourseDTO dto) {
        List<Course> courseList = courseService.findCourseByCondition(dto);
        return new ResponseResult(true, 0, "成功", courseList);
    }

    /**
     * @Author: andreaszhou
     * @Description: 文件上传
     * @DateTime: 2021/7/21 10:02
     * @Params:
     * @Return
     */
    @RequestMapping("/courseUpload")
    public ResponseResult courseUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException();
        }
        // D:\apache-tomcat-8.5.56\webapps\ssm-web\
        String realPath = request.getServletContext().getRealPath("/");
        // D:\apache-tomcat-8.5.56\webapps
        String substring = realPath.substring(0, realPath.indexOf("ssm_web"));
        //lagou.jpg
        String filename = file.getOriginalFilename();
        //12421321.jpg
        String newFileName = System.currentTimeMillis() + filename.substring(filename.lastIndexOf("."));
        // 5.文件上传
        String uploadPath = substring + "upload\\";
        File filePath = new File(uploadPath, newFileName);
        // 如果目录不存在就创建目录,如果当前没有存在新的目录，那么就创建新的目录
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
            System.out.println("创建目录：" + filePath);
        }
        file.transferTo(filePath);
        // 6. 将文件名和文件路径返回，进行响应
        Map<String, String> map = new HashMap<>();
        map.put("fileName", newFileName);
        map.put("filePath", "http://localhost:8080/upload/" + newFileName);
        ResponseResult responseResult = new ResponseResult(true, 200, "图片上传成功", map);
        return responseResult;
    }

    /**
     * @Author: andreaszhou
     * @Description: 新建课程和编辑课程
     * @DateTime: 2021/7/21 14:02
     * @Params:
     * @Return
     */
    @RequestMapping("saveOrUpdateCourse")
    public ResponseResult saveOrUpdateCourse(@RequestBody CourseDTO dto){
        if (dto.getId()==null){
            courseService.saveCourseOrTeacher(dto);
            ResponseResult responseResult = new ResponseResult(true, 200, "新增成功", null);
            return responseResult;
        }else {
            courseService.updateCourseOrTeacher(dto);
            ResponseResult responseResult = new ResponseResult(true, 200, "修改成功", null);
            return responseResult;
        }
    }

    /**
     * @Author: andreaszhou
     * @Description: 课程上下架,修改课程转态
     * @DateTime: 2021/7/21 14:03
     * @Params: dto
     * @Return
     */
    @RequestMapping("/updateCourseStatus")
    public ResponseResult updateCourseStatus(@RequestBody CourseDTO dto){
        courseService.updateCourseStatus(dto);
        ResponseResult responseResult = new ResponseResult(true,200,"修改成功",null);
        return responseResult;
    }
    /**
     * @Author: andreaszhou
     * @Description: 编辑时的回显操作 其实就是简单的回显
     * @DateTime: 2021/7/21 14:14
     * @Params: id
     * @Return 
     */
    @RequestMapping("/findCourseById")
    public ResponseResult findCourseById(@RequestParam("id") Integer id) {
        CourseVO vo = courseService.findCourseById(id);
        ResponseResult responseResult = new ResponseResult(true,200,"查询成功",vo);
        return responseResult;
    }
    /**
     * @Author: andreaszhou
     * @Description: TODO
     * @DateTime: 2021/7/21 14:43
     * @Params: 
     * @Return 
     */
    
}