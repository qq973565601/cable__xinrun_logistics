package org.jeecg;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.util.StrUtil;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan2;
import org.jeecg.modules.cable.importpackage.Plan1Im;
import org.jeecg.modules.demo.mock.MockController;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.system.service.ISysDataLogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleTest {

	@Resource
	private JeecgDemoMapper jeecgDemoMapper;
	@Resource
	private IJeecgDemoService jeecgDemoService;
	@Resource
	private ISysDataLogService sysDataLogService;
	@Resource
	private MockController mock;


	@Test
	public void testSelect1() {
		System.out.println(("----- selectAll method test ------"));
		Plan1 plan1 = new Plan1();
		Plan1 plan2 = new Plan1();
		Plan1 plan3 = new Plan1();
		Plan1 plan4 = new Plan1();
		plan1.setBackup1("backup1");
		plan1.setBackup2("backup2");
		plan1.setBackup3("backup3");
		plan1.setBackup4("backup4");
		plan1.setBackup5("backup5");
		plan2.setBackup1("backup1");
		plan2.setBackup2("backup2");
		plan2.setBackup3("backup3");
		plan2.setBackup4("backup4");
		plan2.setBackup5("backup5");
		plan3.setBackup1("");
		plan3.setBackup2("");
		plan3.setBackup3("");
		plan3.setBackup4("");
		plan3.setBackup5("");

		boolean equals2 = plan1.toString().equals(plan2.toString());
		boolean equals2ss = plan1.equals(plan2);
		boolean equals3 = plan1.toString().equals(plan3.toString());
		boolean equals3ss = plan1.equals(plan3);
		boolean equals4 = plan3.toString().equals(plan4.toString());
		boolean equals4ss = plan3.equals(plan4);

		List<Plan1> list = new ArrayList<>();

		System.err.println("对象 Plan 1 》》》："+ plan1.toString());
		System.err.println("对象 Plan 2 》》》："+ plan2.toString());

		System.err.println("对象 equals2 》》》："+ equals2);
		System.err.println("对象 equals2ss 》》》："+ equals2ss);
		System.err.println("对象 equals3 》》》："+ equals3);
		System.err.println("对象 equals3ss 》》》："+ equals3ss);
		System.err.println("对象 equals4 》》》："+ equals4);
		System.err.println("对象 equals4ss 》》》："+ equals4ss);

	}


	@Test
	public void testSelect() {
		System.out.println(("----- selectAll method test ------"));
		List<JeecgDemo> userList = jeecgDemoMapper.selectList(null);
		Assert.assertEquals(5, userList.size());
		userList.forEach(System.out::println);
	}

	@Test
	public void testXmlSql() {
		System.out.println(("----- selectAll method test ------"));
		List<JeecgDemo> userList = jeecgDemoMapper.getDemoByName("Sandy12");
		userList.forEach(System.out::println);
	}

	/**
	 * 测试事务
	 */
	@Test
	public void testTran() {
		jeecgDemoService.testTran();
	}

	//author:lvdandan-----date：20190315---for:添加数据日志测试----
	/**
	 * 测试数据日志添加
	 */
	@Test
	public void testDataLogSave() {
		System.out.println(("----- datalog test ------"));
		String tableName = "jeecg_demo";
		String dataId = "4028ef81550c1a7901550c1cd6e70001";
		String dataContent = mock.sysDataLogJson();
		sysDataLogService.addDataLog(tableName, dataId, dataContent);
	}
	//author:lvdandan-----date：20190315---for:添加数据日志测试----
}
