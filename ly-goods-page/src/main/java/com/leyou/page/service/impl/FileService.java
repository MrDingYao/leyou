package com.leyou.page.service.impl;

import com.leyou.common.utils.ThreadUtils;
import com.leyou.page.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;

/**
 * @Description //TODO 利用thymeleaf实现页面静态化的service
 * @Author santu
 * @Date 2018 - 10 - 18 19:57
 **/
@Service
public class FileService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private IGoodsService goodsService;

    // C:/Repository/nginx-1.12.2/nginx-1.12.2/html
    @Value("${ly.thymeleaf.destPath}")
    private String destPath;

    /**
     * 创建静态的html页面
     * @param id
     */
    public void createHtml(Long id) throws Exception {
        // 创建上下文
        Context context = new Context();
        // 将数据存入上下文
        context.setVariables(this.goodsService.loadModel(id));
        // 创建临时文件
        File temp = new File(id + ".html");
        // 创建目标文件
        File dest = createPath(id);
        // 创建临时保存文件
        File bak = new File(id + "_bak.html");

        try (PrintWriter printWriter = new PrintWriter(temp, "UTF-8")){
            // 利用模板引擎生成静态化页面
            this.templateEngine.process("item", context, printWriter);

            // 如果目标文件已经存在，则备份至备份文件
            if (dest.exists()) {
                dest.renameTo(bak);
            }
            // 将新的页面覆盖目标文件
            FileCopyUtils.copy(temp, dest);
            // 成功后将备份页面删除
            bak.delete();

        } catch (IOException e) {
            // 如果出现异常，将备份文件还原
            bak.renameTo(dest);
            // 重新抛出异常，声明页面生成失败
            throw new Exception(e);
        } finally {
            // 最终删除临时文件
            temp.delete();
        }
    }

    /**
     * 通过商品的id生成静态化页面的文件目标
     * @param id
     * @return
     */
    private File createPath(Long id) {
        if (id == null) {
            return null;
        }
        File dest = new File(this.destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return new File(dest, id + ".html");
    }

    /**
     * 判断目标文件是否已经存在
     * @param id
     * @return
     */
    public Boolean exists(Long id){
        File dest = createPath(id);
        return dest.exists();
    }

    /**
     * 通过异步线程生成静态化页面
     * @param id
     */
    public void syncCreateHtml(Long id){
        ThreadUtils.executor(() -> {
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 根据商品id删除商品详情的静态页面
     * @param id
     */
    public void deleteHtml(Long id) {
        if (id == null) {
            return;
        }
        File file = new File(this.destPath, id + ".html");
        file.deleteOnExit();
    }

}
