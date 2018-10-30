package com.example.demo;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author lzx
 */
@SpringBootApplication
@EnableScheduling
@Controller
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }



    /**
     * Java7新特性: try-with-resources
     *  1.资源必须实现AutoCloseable接口
     *  2.资源的close方法调用顺序与它们的创建顺序相反
     */
    public void tryWithResources() {

        try (OutputStream out = new FileOutputStream("demo/pom-copy.xml");
             InputStream in = new FileInputStream("demo/pom.xml")) {
            byte[] bytes = new byte[1024];
            int length = -1;
            while ((length = in.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

//        try(A a = new A();
//            B b = new B();) {
//            System.out.println("this is a test...");
//        }catch (Exception e){
//            log.error(e.getMessage(), e);
//        }

    }

    class A implements AutoCloseable{

        @Override
        public void close() throws Exception {
            System.out.println("调用了A.close...");
        }
    }

    class B implements AutoCloseable{

        @Override
        public void close() throws Exception {
            System.out.println("调用了B.close...");
        }
    }



    /**
     * java8新特性: 新的时间和日期API,详见java8 API
     *  1.LocalDate,LocalTime,LocalDateTime是final修饰的,不可变
     */
    public void datetime(){

        LocalDate localDate = LocalDate.now();
        LocalDate localDate2 = LocalDate.of(2018,10,31);
        LocalDate localDate3 = LocalDate.ofYearDay(2018,100);

        log.info("localDate:" + localDate);
        log.info("localDate2:" + localDate2);
        log.info("localDate3:" + localDate3);

        //获取当前日期的年月日等信息
        log.info("year:" + localDate.getYear());
        log.info("year:" + localDate.get(ChronoField.YEAR));
        log.info("year:" + localDate.getLong(ChronoField.YEAR));
        //其他类似于year
        log.info("month:" + localDate.getMonth());
        log.info("monthValue:" + localDate.getMonthValue());
        log.info("dayOfMonth:" + localDate.getDayOfMonth());
        log.info("dayOfWeek:" + localDate.getDayOfWeek());
        log.info("dayOfWeekValue:" + localDate.getDayOfWeek().getValue());
        log.info("dayOfYear:" + localDate.getDayOfYear());

        log.info("该年的天数:" + localDate.lengthOfYear());
        log.info("该月的天数:" + localDate.lengthOfMonth());

        //日期的比较
        log.info("比较日期:" + localDate.compareTo(localDate2));
        log.info("比较日期:" + localDate.isAfter(localDate2));
        log.info("比较日期:" + localDate.isBefore(localDate2));
        log.info("是否相等:" + localDate.equals(localDate2));
        log.info("是否相等:" + localDate.isEqual(localDate2));

        //日期的修改
        log.info("减去指定:" + localDate.minus(2, ChronoUnit.YEARS));
        log.info("减去指定年数:" + localDate.minusYears(2));
        log.info("减去指定月数:" + localDate.minusMonths(2));
        log.info("减去指定周数:" + localDate.minusWeeks(2));
        log.info("减去指定天数:" + localDate.minusDays(2));

        log.info("减去指定时间:" + localDate.minus(Period.of(2,0,0)));
        log.info("减去指定时间:" + localDate.minus(Period.between(localDate,localDate2)));

        //类似于minus,minusYears,...
        log.info("加上指定年数:" + localDate.plus(2,ChronoUnit.YEARS));

        log.info("修改日期:" + localDate.with(TemporalAdjusters.firstDayOfMonth()));
        log.info("修改日期:" + localDate.with(ChronoField.YEAR,2019));
        log.info("修改年份:" + localDate.withYear(2017));
        log.info("修改月份:" + localDate.withMonth(12));
        log.info("修改每个月的第几天:" + localDate.withDayOfMonth(31));
        log.info("修改每年的第几天:" + localDate.withDayOfYear(100));

        //日期的格式化以及解析
        log.info("格式化日期uuuuMMdd:" + localDate.format(DateTimeFormatter.BASIC_ISO_DATE));
        log.info("格式化日期uuuu-MM-dd:" + localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        log.info("格式化日期:" + localDate.format(DateTimeFormatter.ofPattern("uuuuMMdd")));
        log.info("解析日期字符串(uuuu-MM-dd):" + LocalDate.parse("2018-10-26"));
        log.info("解析日期字符串:" + LocalDate.parse("20181026", DateTimeFormatter.ofPattern("uuuuMMdd")));


        //LocalTime类似LocalDate,LocalDateTime是LocalDate和LocalTime的结合体
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("localTime:" + localTime);
        log.info("localDateTime:" + localDateTime);
        localDateTime.minus(Period.ZERO);
        localDateTime.minus(Duration.ZERO);
    }



    /**
     * java8新特性: Optional,详见java8 API
     */
    public void optional(){

        Optional<String> username = Optional.of("lzx");
        Optional<String> empty = Optional.ofNullable(null);
        Optional<String> empty2 = Optional.empty();

        System.out.println(username);
        System.out.println(empty);
        System.out.println(empty2);

        //判断时候有值,有值返回true否则返回false
        if(username.isPresent()){
            //如果有值则返回否则抛出异常
            System.out.println(username.get());
        }

        //如果有值调用函数,否则不做处理
        username.ifPresent(new Consumer<String>() {
            @Override
            public void accept(String value) {
                System.out.println("value length:" + value.length());
            }
        });

        //如果有值将值返回否则返回指定值
        String s1 = empty.orElse("this is null");
        String s2 = username.orElse("this is null");
        System.out.println(s1);
        System.out.println(s2);

        //如果有值将值返回否则返回函数返回值
        s1 = empty.orElseGet(new Supplier<String>() {
            @Override
            public String get() {
                return "this is null";
            }
        });
        s2 = username.orElseGet(new Supplier<String>() {
            @Override
            public String get() {
                return "this is null";
            }
        });
        System.out.println(s1);
        System.out.println(s2);

        //如果有值将值返回否则抛出异常
        try {
            s1 = empty.orElseThrow(new Supplier<Throwable>() {
                @Override
                public Throwable get() {
                    return new Exception("empty is null");
                }
            });
            System.out.println(s1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        try {
            s2 = username.orElseThrow(new Supplier<Throwable>() {
                @Override
                public Throwable get() {
                    return new Exception("username is null");
                }
            });
            System.out.println(s2);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(),throwable);
        }

        /**
         *  1.如果有值,返回调用函数的返回值
         *  2.如果没有值或者函数返回值为null,则返回空Optional
         */
        Optional<String> upperName = username.map(new Function<String, String>() {
            @Override
            public String apply(String value) {
                return value.toUpperCase();
            }
        });
        Optional<String> upperName2 = empty.map(new Function<String, String>() {
            @Override
            public String apply(String value) {
                return value.toUpperCase();
            }
        });
        System.out.println(upperName);
        System.out.println(upperName2);

        //类似map,不同在于map会对函数返回结果封装为Optional,而flatMap必须返回Optional
        upperName = username.flatMap(new Function<String, Optional<String>>() {
            @Override
            public Optional<String> apply(String s) {
                return Optional.empty();
            }
        });
        System.out.println(upperName);

        //如果有值并且满足断言条件返回包含该值的Optional否则返回空Optional
        Optional<String> longName = username.filter(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return value.length() > 2;
            }
        });
        Optional<String> longName2 = Optional.of("a").filter(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return value.length() > 2;
            }
        });
        Optional<String> longName3 = empty.filter(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return value.length() > 2;
            }
        });
        System.out.println(longName.orElse("value length <= 2"));
        System.out.println(longName2.orElse("value length <= 2"));
        System.out.println(longName3.orElse("value length <= 2"));
    }



    /**
     * java8新特性: 函数式接口
     *  1.函数式接口: 只定义了唯一的抽象方法的接口（除了Object对象的public方法）
     *  2.函数式接口允许定义Object的public方法
     *          这些方法对于函数式接口来说,不被当成是抽象方法(虽然它们是抽象方法)
     *          因为任何一个函数式接口的实现,默认都继承了Object类,包含了来自java.lang.Object里对这些抽象方法的实现
     *  3.函数式接口允许包含静态方法
     *  4.函数式接口允许定义默认方法
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void functionInterface(){

    }

    @FunctionalInterface
    interface ObjectFunctionInterface{

        void count(int i);

        boolean equals(Object obj);
        String toString();

        static Integer sum(){
            return null;
        }

        default String say(){
            return "";
        }

    }



    /**
     * java8新特性: Stream
     */
    public void stream(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "jkl");
//        List<String> filtered = strings.stream().filter()
    }


}
