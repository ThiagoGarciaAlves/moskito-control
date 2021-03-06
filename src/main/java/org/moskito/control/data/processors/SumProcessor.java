package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 23:44
 */
public class SumProcessor extends AbstractDataProcessor implements DataProcessor{
	private List<String> attributeNames = new LinkedList<>();

	@Override
	void configureParameter(String parameter) {
		String[] tokens = StringUtils.tokenize(parameter, ',');
		for (String t : tokens){
			attributeNames.add(t.trim());
		}
	}

	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String,String> ret = new HashMap<>();

		double sum = 0;
		for (String name : attributeNames){
			String val = data.get(name);
			if (val==null || val.length()==0)
				continue;
			try{
				double valAsDouble = Double.parseDouble(val);
				sum += valAsDouble;
				System.out.println(name+" = "+valAsDouble+", sum="+sum);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}

		}
		ret.put(getVariableName(), Double.valueOf(sum).toString());
		return ret;
	}
}
