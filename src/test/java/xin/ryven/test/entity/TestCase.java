package xin.ryven.test.entity;

import org.junit.Test;
import xin.ryven.sqltool.generator.MyBatisGenerator;

import java.util.Properties;
import java.util.Set;

public class TestCase {

    @Test
    public void generate() {
        MyBatisGenerator.generate(Retailer.class);
    }

    @Test
    public void cmd() {
        Properties properties = System.getProperties();
        Set<String> strings = properties.stringPropertyNames();
        for (String s : strings) {
            System.out.println(s + " : " + System.getProperty(s));
        }
    }

}
