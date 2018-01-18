package com.keep.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;

public class ImageHepler {
    public static int defalutdropWidth = 150;
    public static int defalutdropHeight = 150;
    /**
     * @Description: 将srcImageFile裁剪后生成destImageFile
     * @param srcImageFile  原始图
     * @param destImageFile  目标图
     * @param width          原始图预处理后width
     * @param height         原始图预处理后height
     * @param rect           目标图输出的格式(rect.x, rect.y, rect.width, rect.height)
     * @throws IOException
     * @author Sun Yanjun
     */
    public static void cut(String srcImageFile, String destImageFile, int width, int height, Rectangle rect) throws IOException {
        Image image = ImageIO.read(new File(srcImageFile));
        BufferedImage bImage = makeThumbnail(image, width, height);

        //把原始图片输出
        //ImageIO.write(bImage, "jpg",new File("img/src2.jpg"));

        saveSubImage(bImage, rect, new File(destImageFile));
    }


    /**
     * @Description: 将srcImageFile裁剪后生成destImageFile
     * @param srcImageFile  原始图
     * @param destImageFile  目标图
     * @param width          原始图预处理后width
     * @param height         原始图预处理后height
     * @param rect           目标图输出的格式(rect.x, rect.y, rect.width, rect.height)
     * @throws IOException
     * @author Sun Yanjun
     */
    public static void cut(File srcImageFile, File destImageFile, int width, int height, Rectangle rect) throws IOException {
        Image image = ImageIO.read(srcImageFile);
        BufferedImage bImage = makeThumbnail(image, width, height);


        saveSubImage(bImage, rect, destImageFile);
    }

    /**
     * @Description: 对原始图片根据(x, y, width, height) = (0, 0, width, height)进行缩放，生成BufferImage
     * @param img
     * @param width 预处理后图片的宽度
     * @param height 预处理后图片高度
     * @return
     * @author Sun Yanjun
     * @throws IOException
     */
    private static BufferedImage makeThumbnail(Image img, int width, int height) throws IOException {
        BufferedImage tag = new BufferedImage(width, height, 1);
        Graphics g = tag.getGraphics();
        g.drawImage(img.getScaledInstance(width, height, 4), 0, 0, null);

        g.dispose();
        return tag;
    }

    /**
     * @Description: 对BufferImage按照(x, y, width, height) = (subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height)进行裁剪
     *               如果subImageBounds范围过大，则用空白填充周围的区域。
     *
     * @param image
     * @param subImageBounds
     * @param destImageFile
     * @throws IOException
     * @author Sun Yanjun
     */
    private static void saveSubImage(BufferedImage image, Rectangle subImageBounds, File destImageFile) throws IOException {
        String fileName = destImageFile.getName();
        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
        BufferedImage subImage = new BufferedImage(subImageBounds.width, subImageBounds.height, 1);
        Graphics g = subImage.getGraphics();


        if ((subImageBounds.width > image.getWidth())
                || (subImageBounds.height > image.getHeight())) {
            int left = subImageBounds.x;
            int top = subImageBounds.y;
            if (image.getWidth() < subImageBounds.width)
                left = (subImageBounds.width - image.getWidth()) / 2;
            if (image.getHeight() < subImageBounds.height)
                top = (subImageBounds.height - image.getHeight()) / 2;
            g.setColor(Color.white);
            g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
            g.drawImage(image, left, top, null);
            ImageIO.write(image, formatName, destImageFile);
        } else {
            g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y,
                    subImageBounds.width, subImageBounds.height), 0, 0, null);
        }
        g.dispose();
        ImageIO.write(subImage, formatName, destImageFile);
    }

    private static void saveSubImage(BufferedImage image, Rectangle subImageBounds,
                                     File subImageFile, int left, int top, int dropWidth, int dropHeight)
            throws IOException {
        if (dropWidth == 0 || dropHeight == 0) {
            dropWidth = defalutdropWidth;
            dropHeight = defalutdropHeight;
        }
        String fileName = subImageFile.getName();
        String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

        BufferedImage subImage = new BufferedImage(dropWidth, dropHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = subImage.getGraphics();

        g.setColor(Color.white);
        g.fillRect(0, 0, dropWidth, dropHeight);
        g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y,
                subImageBounds.width, subImageBounds.height), left, top, null);
        g.dispose();
        File file = new File(subImageFile.getPath());
        if (!file.exists()) {
            file.mkdirs();
        }

        ImageIO.write(subImage, formatName, subImageFile);
    }

    public static void cut(File srcImageFile, File descDir, Rectangle rect, int[] intParms)
            throws IOException {
        Image image = ImageIO.read(srcImageFile);
        BufferedImage bImage = makeThumbnail(image, intParms[0], intParms[1]);

        /*
         * new int[] { imageWidth, imageHeight, cutLeft, cutTop, dropWidth,
         * dropHeight }
         */

        /*
         * String fileName = descDir.getName();
         * System.out.println("fileName:"+fileName); String formatName =
         * fileName.substring(fileName.lastIndexOf(".") + 1);
         * System.out.println("formatName:"+fileName); Iterator<ImageReader> it
         * = ImageIO.getImageReadersByFormatName(formatName); ImageReader reader
         * = it.next(); // 获取图片流 ImageInputStream iis
         * =ImageIO.createImageInputStream(new FileInputStream(srcImageFile));
         * reader.setInput(iis, true); ImageReadParam param =
         * reader.getDefaultReadParam();
         *
         * rect=new Rectangle(intParms[2], intParms[3], intParms[4],
         * intParms[5]); param.setSourceRegion(rect);
         *
         * BufferedImage bi = reader.read(0, param); ImageIO.write(bi,
         * formatName, descDir);
         */

        saveSubImage(bImage, rect, descDir, intParms[2] < 0 ? -intParms[2] : 0,
                intParms[3] < 0 ? -intParms[3] : 0, intParms[4], intParms[5]);
    }
}

 

