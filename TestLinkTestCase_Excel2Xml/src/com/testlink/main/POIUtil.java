package com.testlink.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.web.multipart.MultipartFile;
/**
 * excel读写工具类
 * @author sun.kai
 * 2016年8月21日
 */
public class POIUtil {
    private static Logger logger  = Logger.getLogger(POIUtil.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    /**
     * 读入excel文件，解析后返回
     * @param file
     * @throws IOException
     */
    public static List<String[]> readExcel(String file) throws IOException{
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if(workbook != null){
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行；具体循环从哪一行开始，需要根据excel的表格格式确定，这里是从第7行开始
                for(int rowNum = firstRowNum+6;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    System.out.print(row + "行......");
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }
    public static void checkFile(String filepath) throws IOException{
        //判断文件是否存在
        File file = new File(filepath.trim());
        if(null == file){
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名

//        String fileName = file.getOriginalFilename();
        String fileName = file.getName();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }
    public static Workbook getWorkBook(String filepath) {
        //获得文件名
        File file = new File(filepath.trim());
        String fileName = file.getName();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
//            InputStream is = file.get();
            InputStream is = new FileInputStream(file);
//            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    /**
     * 替换一个字符串中的某些指定字符
     * @param strData String 原始字符串
     * @param regex String 要替换的字符串
     * @param replacement String 替代字符串
     * @return String 替换后的字符串
     */
    public static String replaceString(String strData, String regex,
                                       String replacement)
    {
        if (strData == null)
        {
            return null;
        }
        int index;
        index = strData.indexOf(regex);
        String strNew = "";
        if (index >= 0)
        {
            while (index >= 0)
            {
                strNew += strData.substring(0, index) + replacement;
                strData = strData.substring(index + regex.length());
                index = strData.indexOf(regex);
            }
            strNew += strData;
            return strNew;
        }
        return strData;
    }

    /**
     * 替换字符串中特殊字符
     */
    public static String encodeString(String strData)
    {
        if (strData == null)
        {
            return "";
        }
        strData = replaceString(strData, "&", "&amp;");
        strData = replaceString(strData, "<", "&lt;");
        strData = replaceString(strData, ">", "&gt;");
        strData = replaceString(strData, "&apos;", "&apos;");
        strData = replaceString(strData, "\"", "&quot;");
        return strData;
    }

    /**
     * 还原字符串中特殊字符
     */
    public static String decodeString(String strData)
    {
        strData = replaceString(strData, "&lt;", "<");
        strData = replaceString(strData, "&gt;", ">");
        strData = replaceString(strData, "&apos;", "&apos;");
        strData = replaceString(strData, "&quot;", "\"");
        strData = replaceString(strData, "&amp;", "&");
        return strData;
    }
}
