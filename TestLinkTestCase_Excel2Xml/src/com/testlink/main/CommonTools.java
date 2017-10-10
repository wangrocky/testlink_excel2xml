package com.testlink.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.web.multipart.MultipartFile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.testlink.vo.InputVo;

public class CommonTools 
{
	public void exchangeXml(String filepath,String savepath) throws DocumentException, IOException
	{
		//解析源文件，获取源文件中所有testcase的取值，并存放在arraylist中
		CommonTools ct = new CommonTools();
		ArrayList<InputVo> iv = ct.analysisExcel(filepath);
		Document doc = DocumentHelper.createDocument();
		Util ut = new Util();
		POIUtil pl = new POIUtil();
		//设置根节点
		Element firstpath = doc.addElement("testsuite");
		firstpath.addAttribute("name",iv.get(0).getFirstpath());
		Element node_order = firstpath.addElement("node_order");
		node_order.setText(ut.createId(0));


        //用arraylist来存放所有不重复的二级目录
        ArrayList<Element> secondpathlist = new ArrayList<Element>();
        ArrayList<Element> thirdpathlist = new ArrayList<Element>();
        ArrayList<String> secondpathlistvalue = new ArrayList<String>();
        ArrayList<String> thirdpathlistvalue = new ArrayList<String>();

        //ArrayList<InputVo> iv的index
        int ivINDEX = 0;
        //ArrayList<String> secondpathlist的Index
        int secondpathlistINDEX = 0;
        //ArrayList<String> thirdpathlist 的index
        int thirdpathlistINDEX = 0;
		/**
		 * 循环处理所有的testcase，详细设计如下：
		 * 1、所有testcase的一级目录名称是相同的，取第一个vo的Firstpath即可；
		 * 2、二级目录的处理较复杂，最终目的是把iv中所有不重复的目录取出来作为XML的节点，一共有7个参数（见上方），具体处理逻辑如下：
		 *    A. iv.index>1后的循环，如果inputvo.getSecondpath等于secondpathlistvalue<j=0>，证明是同一个二级目录，无须新建新的二级xml节点，
		 *       如果inputvo.getSecondpath不等于secondpathlist<j=0>，证明有新的二级目录，则新建新的二级xml节点，
		 *       把inputvo.getSecondpath赋值给secondpathlistvalue<j++>，后面的inputvo.getSecondpath则与secondpathlistvalue<j++>做对比，
		 *       并且把当前的element保存到secondpathlist中，后面三级目录可以通过secondpathlist获取到上一级节点的准确位置
		 *    注：由于二级目录在同属于一个一级目录，真实情况下一个特性不允许出现两个名称相同的子特性，而且所有二级目录相同的用例都是连续的，不存在
		 *        secondpathlist<j++>与secondpathlist<i(i<j)>相同的情况，三级目录也是
		 * 3、三级目录的处理同二级目录；
		 * 4、testcase的其他字段分装成一个单独方法insetSourceValueIntoTarget，每次循环调用一次即可。
		 */
		InputVo vifirst = iv.get(0);
        //secondpath赋值
        Element secondpathfirst = firstpath.addElement("testsuite");
        secondpathfirst.addAttribute("name", vifirst.getSecondpath());
        Element node_order_2_first = secondpathfirst.addElement("node_order");
        node_order_2_first.setText("10"+String.valueOf(secondpathlistINDEX+1));
        //secondpathlist第一个元素
        secondpathlist.add(secondpathfirst);
        secondpathlistvalue.add(vifirst.getSecondpath());

        //thirdpath赋值
        Element thirdpath = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
        thirdpath.addAttribute("name", vifirst.getThirdpath());
        ct.insetSourceValueIntoTarget(vifirst, thirdpath,ivINDEX);
        //thirdpathlist第一个元素
        thirdpathlist.add(thirdpath);
        thirdpathlistvalue.add(vifirst.getThirdpath());


		for (InputVo inputvo : iv)
		{
			if (ivINDEX == 0)
			{
				//do nothing
			}

            else if (ivINDEX != 0 && inputvo.getSecondpath().equals(secondpathlistvalue.get(secondpathlistINDEX)))
            {
                if(inputvo.getThirdpath().equals(thirdpathlistvalue.get(thirdpathlistINDEX)))
                {
                    ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
                }
                else
                {
                    //thirdpath赋值
                    Element thirdpathnext = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
                    thirdpathnext.addAttribute("name", inputvo.getThirdpath());
                    thirdpathlist.add(thirdpathnext);
                    thirdpathlistvalue.add(inputvo.getThirdpath());
                    thirdpathlistINDEX++;
                    ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
                }
            }
            else
            {
                Element secondpath = firstpath.addElement("testsuite");
                secondpath.addAttribute("name", inputvo.getSecondpath());
                Element node_order_2 = secondpath.addElement("node_order");
                node_order_2.setText("10"+String.valueOf(secondpathlistINDEX+2));
                secondpathlistvalue.add(inputvo.getSecondpath());
                secondpathlist.add(secondpath);
                secondpathlistINDEX++;

                Element thirdpathnext = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
                thirdpathnext.addAttribute("name", inputvo.getThirdpath());
                thirdpathlist.add(thirdpathnext);
                thirdpathlistvalue.add(inputvo.getThirdpath());
                thirdpathlistINDEX++;
                ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
            }
            ivINDEX++;
		}
		//输出为文件
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GBK");
//		format.setEncoding("UTF-8");
		File file = new File(savepath);
		XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
		writer.write(doc);
	}

	public void replaceTxtByStr(String filepath, String oldStr,String replaceStr) throws IOException
    {
        String temp = "";
        try {
            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该行前面的内容
            for (int j = 1; (temp = br.readLine()) != null
                    && !temp.equals(oldStr); j++) {
                buf = buf.append(temp);
                buf = buf.append(System.getProperty("line.separator"));
            }

            // 将内容插入
            buf = buf.append(replaceStr);

            // 保存该行后面的内容
            while ((temp = br.readLine()) != null) {
                buf = buf.append(System.getProperty("line.separator"));
                buf = buf.append(temp);
            }

            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
            System.out.print("replace over..");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	public ArrayList<InputVo> analysisXml(String filepath) throws DocumentException
	{
		ArrayList<InputVo> iv = new ArrayList<InputVo>();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(filepath));
		Element root = doc.getRootElement();
		Iterator it = root.elementIterator();
		
		while (it.hasNext())
		{
			//获取每个testcase的所有字段取值并赋值给InputVo，然后放入List中存放
			Element element = (Element) it.next();
			InputVo inputvo = new InputVo();
			inputvo.setFirstpath(element.elementText("firstpath"));
			inputvo.setSecondpath(element.elementText("secondpath"));
			inputvo.setThirdpath(element.elementText("thirdpath"));
			inputvo.setName(element.elementText("name"));
			inputvo.setExternalid(element.elementText("externalid"));
			inputvo.setValue(element.elementText("value"));
			inputvo.setPreconditions(element.elementText("preconditions"));
			inputvo.setActions(element.elementText("actions"));
			inputvo.setExpectedresults(element.elementText("expectedresults"));
			inputvo.setExecutiontype(element.elementText("executiontype"));
			iv.add(inputvo);
		}
		return iv;
	}
	
	public void insetSourceValueIntoTarget(InputVo inputvo,Element el,int testcaseid)
	{
		//定义文件输出的整体xml结构
		Element testcase = el.addElement("testcase");
		//testcase xml结构
		Element node_order = testcase.addElement("node_order");
		Element externalid = testcase.addElement("externalid");
		Element version = testcase.addElement("version");
		Element summary = testcase.addElement("summary");
		Element preconditions = testcase.addElement("preconditions");
		Element execution_type = testcase.addElement("execution_type");
		Element importance = testcase.addElement("importance");
		Element steps = testcase.addElement("steps");
//		Element custom_fields = testcase.addElement("custom_fields");
		//step xml结构
		Element step = steps.addElement("step");
		Element step_number = step.addElement("step_number");
		Element actions = step.addElement("actions");
		Element expectedresults = step.addElement("expectedresults");
		Element execution_type2 = step.addElement("execution_type");
		//custom_field xml结构
//		Element custom_field = custom_fields.addElement("custom_field");
//		Element name = custom_field.addElement("name");
//		Element value = custom_field.addElement("value");
		
		//生成一个全局唯一的id，供所有要用到Id的字段
		Util ul = new Util();
		String commonId = ul.createId(testcaseid);
		
		//将inputvo中的值取出来赋给要输出xml节点的值
		testcase.addAttribute("id", commonId);
		testcase.addAttribute("name", inputvo.getName());
		node_order.setText("");
		externalid.setText(inputvo.getExternalid());
		version.setText(ul.addCDATA(commonId));
		summary.setText(ul.addCDATA(inputvo.getName()));
//		importance.setText(ul.addCDATA(inputvo.getImportance()));
		preconditions.setText(ul.addHtmlElement(inputvo.getPreconditions()));
//		System.out.print(inputvo.getExecutiontype());
		if (inputvo.getExecutiontype().equals("手工"))
        {
            execution_type.setText(ul.addCDATA("1"));
        }
        if (inputvo.getExecutiontype().equals("自动的"))
        {
            execution_type.setText(ul.addCDATA("2"));
        }
        else {
            execution_type.setText(ul.addCDATA("1"));
        }
//		execution_type.setText(ul.addCDATA("1"));
//		importance.setText(ul.addCDATA("2"));
        if (inputvo.getImportance().equals("高"))
        {
            importance.setText(ul.addCDATA("3"));
        }
        if (inputvo.getImportance().equals("中"))
        {
            importance.setText(ul.addCDATA("2"));
        }
        if (inputvo.getImportance().equals("低"))
        {
            importance.setText(ul.addCDATA("1"));
        }
        else {
            importance.setText(ul.addCDATA("3"));
        }
		//step结构体赋值
		step_number.setText("1");
		actions.setText(ul.addHtmlElement(inputvo.getActions()));
		expectedresults.setText(ul.addHtmlElement(inputvo.getExpectedresults()));

        if (inputvo.getExecutiontype().equals("手工"))
        {
            execution_type2.setText(ul.addCDATA("1"));
        }
        if (inputvo.getExecutiontype().equals("自动的"))
        {
            execution_type2.setText(ul.addCDATA("2"));
        }
		//execution_type2.setText(ul.addCDATA("1"));
		//custom_field结构体赋值
//		name.setText("testcase_level");
//		value.setText(inputvo.getValue());
	}
	
	public ArrayList<InputVo> analysisExcel(String filepath) throws  IOException
	{
		ArrayList<InputVo> iv = new ArrayList<InputVo>();
//		POIUtil poiUtil = new POIUtil();
		Workbook workbook = POIUtil.getWorkBook(filepath);
//		Workbook workbook = new XSSFWorkbook();
//		InputStream is = new FileInputStream(new File((filepath)));
//        XSSFWorkbook workbook = new XSSFWorkbook(is);
//        Workbook workbook = new Workbook(is);
//        Workbook workbook = POIUtil.getWorkBook(filepath)
//		XSSFWorkbook workbook = XSSFWorkbook.getWorkbook(new File(filepath));
		Sheet sheet = workbook.getSheetAt(0);
		for (int role = 6;role<=sheet.getLastRowNum();role++)
		{
//		    XSSFRow xssfRow = sheet.getRow(role);
            Row row = sheet.getRow(role);
            Cell firstpath = row.getCell(1);
//            Cell firstpath = sheet.get
			// 采用张小东的excel用例模板，第2列是“项目”，作为一级路径
//			XSSFCell firstpath = sheet.getCell(1, role);
//            XSSFCell firstpath = xssfRow.getCell(1);
			if (POIUtil.getCellValue(firstpath)!=null&&!POIUtil.getCellValue(firstpath).equals(""))
			{
			    // 第3列是“模块”，作为2级路径
				Cell secondpath = row.getCell(2);
//                XSSFCell secondpath = xssfRow.getCell(2);
				// 第4列是“子模块”，作为3级路径
				Cell thirdpath = row.getCell(3);
//				Cell thirdpath = sheet.getCell(2, role);
                //第5列是用例标题
				Cell name = row.getCell(4);
//                XSSFCell name = xssfRow.getCell(3);
				//第1列是用例编号
				Cell externalid = row.getCell(0);
//                XSSFCell externalid = xssfRow.getCell(0);
//				Cell value = sheet.getCell(5, role);
                //第9列是用例优先级
				Cell importance = row.getCell(8);
//                XSSFCell importance = xssfRow.getCell(7);
				//张小东用例模板中，没有预置条件，添加到第6列
				Cell preconditions = row.getCell(5);
//                XSSFCell preconditions = xssfRow.getCell(4);
                // 第7列是操作步骤
				Cell actions = row.getCell(6);
//                XSSFCell actions = xssfRow.getCell(5);
				// 第8列是期望结果
				Cell expectedresults = row.getCell(7);
//                XSSFCell expectedresults = xssfRow.getCell(6);
				// 第10列是执行类型，是手工还是自动化
				Cell executiontype = row.getCell(9);
//                XSSFCell executiontype = xssfRow.getCell(11);
				InputVo inputvo = new InputVo();
//				inputvo.setFirstpath(firstpath.getContents());
				inputvo.setFirstpath(POIUtil.getCellValue(firstpath));
//				inputvo.setFirstpath(firstpath.getStringCellValue());

				inputvo.setSecondpath(POIUtil.getCellValue(secondpath));
				inputvo.setThirdpath(POIUtil.getCellValue(thirdpath));
//				inputvo.setSecondpath(secondpath.getStringCellValue());
//				inputvo.setThirdpath(thirdpath.getContents());
//				inputvo.setName(name.getStringCellValue());
				inputvo.setName(POIUtil.encodeString(name.getStringCellValue()));
//				inputvo.setName(name.getStringCellValue());
				inputvo.setExternalid(externalid.getStringCellValue());
				inputvo.setImportance(importance.getStringCellValue());
//				inputvo.setValue(value.getContents());
				inputvo.setPreconditions(preconditions.getStringCellValue());
				inputvo.setActions(POIUtil.encodeString(actions.getStringCellValue()));
//				inputvo.setActions(actions.getStringCellValue());
				inputvo.setExpectedresults(expectedresults.getStringCellValue());
				inputvo.setExecutiontype(executiontype.getStringCellValue());
				iv.add(inputvo);
			}
		}
		return iv;
	}
}
