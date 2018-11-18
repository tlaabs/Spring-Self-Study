package springbook.user.sqlservice.jaxb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class JaxbTest {
	@Test
	public void readSqlmap() throws JAXBException, IOException{
		String contextPath = Sqlmap.class.getPackage().getName();
		JAXBContext context = JAXBContext.newInstance(contextPath);
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
				getClass().getResourceAsStream("sqlmap.xml")); //테스트 클래스와 같은 폴더에 있는 XML 사용
		
		List<SqlType> sqlList = sqlmap.getSql();
		
		assertThat(sqlList.size(), is(2));
		assertThat(sqlList.get(0).getKey(), is("add"));
		assertThat(sqlList.get(0).getValue(), is("insert"));
		
	}
}
