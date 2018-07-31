package com.artifex.mupdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.PointF;

import com.hoperun.mipmanager.utils.ConstState;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFUtilFromiText
{
    /**
     * 90度
     */
    public static final int     ROTATE_ANGLE  = 90;
    
    /**
     * 0度
     */
    public static final int     TEXT_ANGLE    = 0;
    
    // private final static String USER_PASSWORD_STRING = "user";
    /**
     * 时间
     */
    private static final String BASE_DATE     = "2013-01-01";
    
    /**
     * 初始化变量
     */
    private FileUtil            mFileUtil     = new FileUtil();
    
    /**
     * 密码
     */
    private String              mPassword     = null;
    
    /** 
    * 
    */
    private PDFLogicUtil        mPDFLogicUtil = new PDFLogicUtil(null);
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param password 密码
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setPassword(String password)
    {
        mPassword = password;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pPDFPath 路径
     * @param pPageNum 页数
     * @return
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public synchronized int rotate(String pPDFPath, int pPageNum)
    {
        int result = -1;
        PdfReader reader = null;
        PdfStamper stp = null;
        String dst = getSDCardPath(pPDFPath) + getSaveFileNamePath(pPDFPath);
        FileOutputStream out = null;
        
        try
        {
            out = new FileOutputStream(dst);
            
            if (mPassword != null)
            {
                reader = new PdfReader(pPDFPath, mPassword.getBytes());
                // stp = new PdfStamper(reader, out, 'p', true);
                stp = new PdfStamper(reader, out, 'p', false);
            }
            else
            {
                reader = new PdfReader(pPDFPath);
                stp = new PdfStamper(reader, out);
            }
            
            // int currentAngle = 0;
            // if (reader.getPageN(pPageNum).getAsNumber(PdfName.ROTATE) !=
            // null) {
            // currentAngle = reader.getPageN(pPageNum)
            // .getAsNumber(PdfName.ROTATE).intValue();
            // }
            
            int currentAngle = reader.getPageRotation(pPageNum);
            
            currentAngle += 90;
            currentAngle %= 360;
            reader.getPageN(pPageNum).put(PdfName.ROTATE, new PdfNumber(currentAngle));
            
            result = 0;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
                if (stp != null)
                    stp.close();
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        // if (mPassword != null) {
        // setKeywords(dst);
        // }
        
        mFileUtil.delete(pPDFPath);
        mFileUtil.rename(dst, pPDFPath);
        
        return result;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pPDFPath pPDFPath
     * @param text text
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public int drawText(String pPDFPath, SignText text)
    {
        PdfReader reader = null;
        PdfStamper stamp = null;
        String dst = getSaveFileNamePath(pPDFPath);
        FileOutputStream out;
        int result = -1;
        // float maxWidth = 0f; 未使用数据
        
        try
        {
            dst = getSDCardPath(pPDFPath) + dst;
            out = new FileOutputStream(dst);
            
            if (mPassword != null)
            {
                reader = new PdfReader(pPDFPath, mPassword.getBytes());
                stamp = new PdfStamper(reader, out, 'p', true);
            }
            else
            {
                reader = new PdfReader(pPDFPath);
                stamp = new PdfStamper(reader, out);
            }
            
            if (text != null)
            {
                int pageNum = text.getPageNum();
                float fontSize = text.getFontSize();
                List<String> textLines = text.getTextLines();
                PointF pointF = text.getLocation();
                int color = text.getFontColor();
                String[] strs = text.getDatename().split("\n");
                float maxStringWidth =
                    mPDFLogicUtil.getMaxStringWidthFromLines(textLines, PDFLogicUtil.FONT_DEFAULT_SIZE);
                
                float x = pointF.x;
                float y = pointF.y;
                
                if (textLines != null && textLines.size() > 0)
                {
                    
                    for (int l = 0; l < textLines.size(); l++)
                    {
                        String content = textLines.get(l);
                        
                        y -= fontSize;// * 1.5f;
                        
                        // 文字水印
                        PdfContentByte over = stamp.getOverContent(pageNum);
                        over.beginText();
                        // BaseFont bf =
                        // BaseFont.createFont(BaseFont.HELVETICA,
                        // BaseFont.WINANSI, BaseFont.EMBEDDED);
                        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                        
                        over.setFontAndSize(bf, fontSize);
                        over.setTextMatrix(0, 0);
                        // over.setRGBColorFill(255, 0, 0);
                        
                        over.setColorFill(new BaseColor(color));
                        // over.setTextRise(pFontSize);
                        over.showTextAlignedKerned(Element.ALIGN_LEFT, content, x, y, TEXT_ANGLE);
                        over.endText();
                        
                        y -= 2f;
                    } // end for
                    
                    float datenameX =
                        maxStringWidth - mPDFLogicUtil.getStringWidth(strs[1], PDFLogicUtil.FONT_DEFAULT_SIZE);
                    if (datenameX <= 0)
                    {
                        x += mPDFLogicUtil.getStringWidth("GG", PDFLogicUtil.FONT_DEFAULT_SIZE);
                    }
                    else
                    {
                        x += datenameX;
                    }
                    
                    y -= (fontSize + 2f);
                    
                    for (int i = 0; i < strs.length; i++)
                    {
                        y -= fontSize;
                        PdfContentByte over = stamp.getOverContent(pageNum);
                        over.beginText();
                        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                        over.setFontAndSize(bf, fontSize);
                        over.setTextMatrix(0, 0);
                        // over.setRGBColorFill(255, 0, 0);
                        // over.setRGBColorFill(color&0xFFFF0000, color&0xFF00FF00, color&0xFF0000FF);
                        over.setColorFill(new BaseColor(color));
                        over.showTextAlignedKerned(Element.ALIGN_LEFT, strs[i], x, y, TEXT_ANGLE);
                        over.endText();
                        
                        y -= 2f;
                    }
                    
                    // float minWidth = getMinWidth();
                    //
                    // if (minWidth >= maxWidth) {
                    // x += 6 * fontSize/2f;
                    // } else {
                    // x += (maxWidth - mPDFLogicUtil.getStringWidth(BASE_DATE, 15)) / 2f;
                    // }
                    
                    // String[] datename = text.getDataname().split("\n");
                    // y -= 4f * fontSize;
                    //
                    // for (String dn : datename) {
                    // PdfContentByte over = stamp.getOverContent(pageNum);
                    // over.beginText();
                    // BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                    // over.setFontAndSize(bf, fontSize);
                    // over.setTextMatrix(0, 0);
                    // over.setRGBColorFill(255, 0, 0);
                    // over.showTextAlignedKerned(Element.ALIGN_LEFT, dn, x, y, TEXT_ANGLE);
                    // over.endText();
                    //
                    // y -= (fontSize * 1.5f);
                    // }
                    
                } // end if
            }
            
            result = 0;
        }
        catch (IOException e)
        {
            result = -1;
            Laf.i(e.getMessage());
        }
        catch (DocumentException e)
        {
            result = -1;
            Laf.i(e.getMessage());
        }
        finally
        {
            try
            {
                if (stamp != null)
                    stamp.close();
                if (reader != null)
                    reader.close();
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        // Laf.i("dst=" + dst);
        // if (mPassword != null) {
        // setKeywords(dst);
        // }
        
        mFileUtil.delete(pPDFPath);
        mFileUtil.rename(dst, pPDFPath);
        
        return result;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pPDFPath pPDFPath
     * @param pPoint pPoint
     * @param bytes bytes
     * @param pPageNum pPageNum
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public int drawImage(String pPDFPath, PointF pPoint, byte[] bytes, int pPageNum)
    {
        PdfReader reader = null;
        PdfStamper stamp = null;
        String dst = null;
        int result = -1;
        
        try
        {
            if (mPassword != null)
            {
                reader = new PdfReader(pPDFPath, mPassword.getBytes());
            }
            else
            {
                reader = new PdfReader(pPDFPath);
            }
            
            dst = getSaveFileNamePath(pPDFPath);
            dst = getSDCardPath(pPDFPath) + dst;
            stamp = new PdfStamper(reader, new FileOutputStream(dst));
            
            Image img = Image.getInstance(bytes);
            img.setAbsolutePosition(pPoint.x, pPoint.y);
            PdfContentByte under = stamp.getOverContent(pPageNum);// .getUnderContent(pPageNum);
            
            under.addImage(img);
            
            result = 0;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (stamp != null)
                    stamp.close();
                if (reader != null)
                    reader.close();
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        mFileUtil.delete(pPDFPath);
        mFileUtil.rename(dst, pPDFPath);
        return result;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pPDFPath pPDFPath
     * @param pPageNum pPageNum
     * @return RealPDF
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public RealPDF getRealPDF(String pPDFPath, int pPageNum)
    {
        PdfReader reader = null;
        RealPDF pdf = null;
        
        try
        {
            if (mPassword != null)
            {
                reader = new PdfReader(pPDFPath, mPassword.getBytes());
            }
            else
            {
                reader = new PdfReader(pPDFPath);
            }
            
            int currentAngle = reader.getPageRotation(pPageNum);
            Rectangle rectangle = reader.getPageSize(pPageNum);
            pdf = new RealPDF();
            
            pdf.width = rectangle.getWidth();
            pdf.height = rectangle.getHeight();
            
            if (currentAngle == 90 || currentAngle == 270)
            {
                float tmp = pdf.width;
                pdf.width = pdf.height;
                pdf.height = tmp;
            }
            
        }
        catch (IOException e)
        {
            pdf = null;
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
        
        return pdf;
    }
    
    // /**
    // * <一句话功能简述>
    // * @Description<功能详细描述>
    // *
    // * @return
    // * @LastModifiedDate：2013-10-18
    // * @author ren_qiujing
    // * @EditHistory：<修改内容><修改人>
    // */
    // private float getMinWidth()
    // {
    // return mPDFLogicUtil.getStringWidth("騥騥騥" + BASE_DATE, 15);
    // }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pPDFPath pPDFPath
     * @return String
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private String getSDCardPath(String pPDFPath)
    {
        // String sdcardPathString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        // String path = sdcardPathString + "MOA/tmp/";
        String path = ConstState.MIP_ROOT_DIR + "tmp/";
        
        File file = new File(path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        
        return path;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pPDFPath pPDFPath
     * @return String
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private String getSaveFileNamePath(String pPDFPath)
    {
        if (pPDFPath != null && pPDFPath.length() > 0)
        {
            int fileIndex = pPDFPath.lastIndexOf("/");
            String fileName = pPDFPath.substring(fileIndex + 1);
            int end = fileName.lastIndexOf(".");
            if (end != -1)
            {
                fileName = fileName.substring(0, end);
            }
            else
            {
                return fileName + "_save";
            }
            // return name + "_" + System.currentTimeMillis() + ".pdf";
            return fileName + "_save.pdf";
        }
        
        return "/tmp/invalid.dat";
    }
    
    // private void setKeywords(String file) {
    // String dst = getSDCardPath(file) + getSaveFileNamePath(file);
    //
    // try {
    // PdfReader reader = new PdfReader(file);
    // int n = reader.getNumberOfPages();
    // Document document = new Document();
    // PdfWriter writer = PdfWriter.getInstance(document,
    // new FileOutputStream(dst));
    //
    // writer.setEncryption("userpassword".getBytes(),
    // mPassword.getBytes(), PdfWriter.ALLOW_MODIFY_ANNOTATIONS
    // | PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING,
    // PdfWriter.STANDARD_ENCRYPTION_128);
    //
    // document.open();
    //
    // PdfContentByte cb = writer.getDirectContent();
    // PdfImportedPage page;
    //
    // int rotation;
    // int i = 0;
    //
    // while (i < n) {
    // i++;
    // document.setPageSize(reader.getPageSizeWithRotation(i));
    // document.newPage();
    // page = writer.getImportedPage(reader, i);
    // rotation = reader.getPageRotation(i);
    //
    // switch (rotation) {
    // case 0:
    // cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
    // break;
    // case 90:
    // cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader
    // .getPageSizeWithRotation(i).getHeight());
    // break;
    // case 180:
    // cb.addTemplate(page, -1f, 0, 0, -1f, reader
    // .getPageSizeWithRotation(i).getWidth(), reader
    // .getPageSizeWithRotation(i).getHeight());
    // break;
    // case 270:
    // cb.addTemplate(page, 0, 1f, -1f, 0, reader
    // .getPageSizeWithRotation(i).getWidth(), 0);
    // break;
    //
    // default:
    // break;
    // }
    // }
    //
    // document.close();
    // writer.close();
    // reader.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // mFileUtil.delete(file);
    // mFileUtil.rename(dst, file);
    // }
    
    private final class FileUtil
    {
        // public int copy(String src, String dst) {
        // FileInputStream _FileInputStream = null;
        // FileOutputStream _FileOutputStream = null;
        // int result = -1;
        //
        // int length = 0;
        // byte[] buff = new byte[4096];
        //
        // try {
        // _FileInputStream = new FileInputStream(src);
        // _FileOutputStream = new FileOutputStream(dst);
        //
        // while ((length = _FileInputStream.read(buff)) != -1) {
        // _FileInputStream.read(buff, 0, length);
        // _FileOutputStream.flush();
        // }
        //
        // result = 0;
        // } catch (FileNotFoundException e) {
        // } catch (IOException e) {
        // } finally {
        // try {
        // _FileInputStream.close();
        // _FileOutputStream.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        //
        // return result;
        // }
        
        /**
         * <一句话功能简述>
         * 
         * @Description<功能详细描述>
         * 
         * @param src src
         * @return int
         * @LastModifiedDate：2013-10-18
         * @author ren_qiujing
         * @EditHistory：<修改内容><修改人>
         */
        public int delete(String src)
        {
            int result = -1;
            File file = new File(src);
            
            if (file.exists())
            {
                result = file.delete() ? 0 : -1;
            }
            
            return result;
        }
        
        /**
         * <一句话功能简述>
         * 
         * @Description<功能详细描述>
         * 
         * @param src src
         * @param newName newName
         * @return int
         * @LastModifiedDate：2013-10-18
         * @author ren_qiujing
         * @EditHistory：<修改内容><修改人>
         */
        public int rename(String src, String newName)
        {
            int result = -1;
            File file = new File(src);
            
            if (file.exists())
            {
                result = file.renameTo(new File(newName)) ? 0 : -1;
            }
            
            return result;
        }
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param src src
     * @param dst dst
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public int changePDFFile(String src, String dst)
    {
        int result = -1;
        
        // result = mFileUtil.delete(src);
        result = mFileUtil.rename(dst, src);
        
        return result;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param src src
     * @return String
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getTempAbsolutePath(String src)
    {
        return getSDCardPath(src) + getSaveFileNamePath(src);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param src src
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public int checkPdfFile(String src)
    {
        int result = -1;
        int n = 0;
        Document document = null;
        PdfReader reader = null;
        
        try
        {
            if (mPassword != null)
            {
                reader = new PdfReader(src, mPassword.getBytes());
            }
            else
            {
                reader = new PdfReader(src);
            }
            
            if (reader != null)
            {
                document = new Document(reader.getPageSize(1));
                document.open();
                n = reader.getNumberOfPages();
                
                if (n != 0)
                    result = 0;
            }
            
        }
        catch (Exception e)
        {
            return -1;
        }
        finally
        {
            if (document != null)
                document.close();
            if (reader != null)
                reader.close();
        }
        
        return result;
    }
}
