package com.zhaoxinms.contract.conf.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.zhaoxinms.common.config.RuoYiConfig;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.common.utils.uuid.IdUtils;
import com.zhaoxinms.contract.conf.domain.vo.ContractFileVo;
import com.zhaoxinms.contract.conf.exception.ServiceException;

/**
 * 文件打包下载
 * 
 * @author huibi
 *
 */
public class ZipDownloadUtil {

    /**
     * 多文件批量打包下载
     * 
     * @param zipFilePath zip文件路径
     * @param paths 要打包的文件
     * @return 打包后的路径
     * @throws IOException
     */
    public static String batchDownload(String zipFilePath, List<ContractFileVo> paths) throws IOException {

        File zippath = new File(zipFilePath);
        if (!zippath.exists()) {
            zippath.mkdirs();
        }
        File file = new File(zipFilePath + File.separator + IdUtils.fastUUID() + "." + "zip");
        file.createNewFile();

        if (paths.size() != 0) {

            // 压缩输出流,包装流,将临时文件输出流包装成压缩流,将所有文件输出到这里,打成zip包
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file.getAbsolutePath()));
            // 循环调用压缩文件方法,将一个一个需要下载的文件打入压缩文件包
            int count = 1;
            for (ContractFileVo path : paths) {
                if (count >= 99) {
                    throw new ServiceException("文件数超过限制，最多打包" + count + "个文件");
                }

                // 文件完整路径
                String pathFile = path.getFileName().replaceAll("/profile", "");
                String realPath = RuoYiConfig.getProfile() + pathFile;
                // 调用压缩方法在下面定义
                fileToZip(realPath, zipOut, path.getOriginalName(), count++);
            }
            zipOut.close();
        }

        return file.getAbsolutePath();
    }

    /**
     * 文件打包为zip
     * 
     * @param filePath 文件路径
     * @param zipOut zip文件
     * @param showName 打包后的显示名
     * @param count 计数器
     * @throws IOException
     */
    private static void fileToZip(String filePath, ZipOutputStream zipOut, String showName, int count)
        throws IOException {
        // 获取文件名称,如果有特殊命名需求,可以将参数列表拓展,传fileName
        String documents = showName;
        FileInputStream fileInput = new FileInputStream(filePath);
        // 缓冲
        byte[] bufferArea = new byte[1024 * 10];
        BufferedInputStream bufferStream = new BufferedInputStream(fileInput, 1024 * 10);
        // 将当前文件作为一个zip实体写入压缩流,fileName代表压缩文件中的文件名称
        String num = StringUtils.leftPad(String.valueOf(count), 2, "0") + ".";
        zipOut.putNextEntry(new ZipEntry(num + documents));
        int length = 0;
        // 最常规IO操作,不必紧张
        while ((length = bufferStream.read(bufferArea, 0, 1024 * 10)) != -1) {
            zipOut.write(bufferArea, 0, length);
        }
        // 关闭流
        fileInput.close();
        // 需要注意的是缓冲流必须要关闭流,否则输出无效
        bufferStream.close();
    }
}
