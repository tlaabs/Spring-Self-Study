package springbook.user.service;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut{

	public void setMappedClassName(String mappedClassName) {
		// TODO Auto-generated method stub
		this.setClassFilter(new SimpleClassFilter(mappedClassName));
	}		
	
	static class SimpleClassFilter implements ClassFilter{
		String mappedName;
		
		private SimpleClassFilter(String mappedName) {
			this.mappedName = mappedName;
		}

		@Override
		public boolean matches(Class<?> clazz) {
			// TODO Auto-generated method stub
			return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
		}
		
				
	}
		
}
