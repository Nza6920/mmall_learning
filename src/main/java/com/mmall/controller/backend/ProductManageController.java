package com.mmall.controller.backend;


import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService = null;

    @Autowired
    private IProductService iProductService = null;

    @Autowired
    private IFileService iFileService = null;

    /**
     * 新增或更新产品
     * @param session   会话
     * @param product   产品
     * @return ServerResponse
     */
    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录管理员");
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 设置产品状态
     * @param session 会话
     * @param productId 产品id
     * @param status 状态
     * @return ServerResponse
     */
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录管理员");
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取产品详细信息
     * @param session 会话
     * @param productId 产品 id
     * @return ServerResponse
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录管理员");
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 后台产品列表
     * @param session   会话
     * @param pageNum   当前页数
     * @param pageSize  每一页的数据量
     * @return ServerResponse
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录管理员");
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑

            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 根据 产品名 或 id 搜索产品
     * @param session     会话
     * @param productName 产品名
     * @param productId   产品id
     * @param pageNum     当前页数
     * @param pageSize    每一页的数据量
     * @return ServerResponse
     */
    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,
                                        String productName,
                                        Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录管理员");
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 上传文件到 ftp 服务器
     * @param file 文件
     * @param request 请求
     * @return ServerResponse
     */
    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(
            @RequestParam(value = "upload_file", required = false) MultipartFile file,
            HttpServletRequest request, HttpSession session)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录管理员");
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑
            String path = request.getSession().getServletContext().getRealPath("upload");
            System.out.println(path);

            String targetFileName = iFileService.upload(file, path);

            // 文件的url
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 富文本文件上传
     * @param file     上传的文件
     * @param request  请求
     * @param session  会话
     * @param response 请求
     * @return Map
     */
    @RequestMapping(value = "rich_text_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richTextImgUpload(
            @RequestParam(value = "upload_file", required = false) MultipartFile file,
            HttpServletRequest request,
            HttpSession session,
            HttpServletResponse response)
    {
        Map resultMap = Maps.newHashMap();

        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }

        // 判断用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 业务逻辑
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);

            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }

            // 文件的url
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);

            // 修改 header
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }

    }
}
