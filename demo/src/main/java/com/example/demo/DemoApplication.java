package com.example.demo;

import lombok.Data;
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
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
     * java8新特性: 函数式接口FunctionInterface
     *  1.函数式接口: 只定义了唯一的抽象方法的接口（除了Object对象的public方法）
     *  2.函数式接口允许定义Object的public方法
     *          这些方法对于函数式接口来说,不被当成是抽象方法(虽然它们是抽象方法)
     *          因为任何一个函数式接口的实现,默认都继承了Object类,包含了来自java.lang.Object里对这些抽象方法的实现
     *  3.函数式接口允许包含静态方法
     *  4.函数式接口允许定义默认方法
     */
    public void functionInterface(){
        Predicate<String> p = value -> value.length()>2;
        log.info(p.test("hello") + "");
        log.info(p.test("hi") + "");

        Consumer<String> c = value -> System.out.println();
        //输出为空
        c.accept("hello");

        c = value -> System.out.println(value);
        //输出hello
        c.accept("hello");

        c = System.out::println;
        //输出hello
        c.accept("hello");

        Supplier<String>  supp = () -> "Supplier";
        log.info(supp.get());

        BinaryOperator<String> bina = (x,y) -> x + " " + y;
        log.info(bina.apply("hello", "world"));

        System.exit(0);
    }

    @FunctionalInterface
    interface ObjectFunctionInterface{

        void count(int i);

        @Override
        boolean equals(Object obj);
        @Override
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
    @Scheduled(cron = "0/3 * * * * ?")
    public void stream(){

        /**
         *  Stream的操作类型:
         *
         *      1.Intermediate(一个流可以后面跟随零个或多个intermediate操作.
         *                  其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用)
         *          主要有map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
         *
         *      2.Terminal(一个流只能有一个terminal操作,当这个操作执行后,流就被使用“光”了,无法再被操作,所以这必定是流的最后一个操作)
         *          主要有forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
         *
         *      3.short-circuiting(
         *          对于一个intermediate操作，如果它接受的是一个无限大（infinite/unbounded）的 Stream,但返回一个有限的新 Stream.
         *          对于一个terminal操作，如果它接受的是一个无限大的 Stream,但能在有限的时间计算出结果。
         *      )
         *
        */

        //构建流的方法,这里这是其中之一
        Stream<Integer> stream1 = Stream.of(1);
        Stream<String> stream2 = Stream.of("hello");
        Stream<String> stream3 = Stream.of("hello","world");
        Stream<Integer> stream4 = Stream.of(1,2,3,5);
        Stream<Integer> stream5 = Stream.concat(stream1, stream4);

        //map,mapToInt,distinct的用法
        List<Integer> numbers = Arrays.asList(3,2,2,3,7,3,5);
        List<Integer> squareList = numbers.stream().map(x -> x*x).distinct().collect(Collectors.toList());
        System.out.println(squareList);
        Stream.of("1","2","3").mapToInt(Integer::parseInt).forEach(System.out::println);

        //flatMap的用法
        Stream.of(Arrays.asList(1,2), Arrays.asList("hello",4)).forEach(System.out::println);
        //将两个list合并为一个list
        List<Object> list =Stream.of(Arrays.asList(1,2), Arrays.asList("hello",4)).flatMap(Collection::stream).collect(Collectors.toList());
        list.stream().limit(4).forEach(System.out::println);

        Stream.of(new C("gailun",18,"male"),new C("kate",16,"female"))
                .flatMap(x -> Stream.of(x.getName())).forEach(System.out::println);

        //sorted,limit的用法
        new Random().ints(5).sorted().forEach(System.out::println);
        System.out.println("===========");
        new Random().ints().limit(10).sorted().forEach(System.out::println);

        //filter的用法
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "jkl");
        List<String> filtered = strings.stream().filter(value -> !value.isEmpty()).collect(Collectors.toList());
        System.out.println(">>>>>" + strings);
        System.out.println(">>>>>" + filtered);

        //peek的用法,类似于map,不过map参数是Function,peek参数是Consumer
        List<Integer> integers = Arrays.asList(1,2,3,4);
        List<C> cs = integers.stream().map(x -> new C("a",x,"male")).peek(x -> x.setName("b")).collect(Collectors.toList());
        System.out.println(cs);

        //skip的用法,舍弃前n个元素的流,结合limit使用
        integers = Arrays.asList(1,2,3,4,5,6,7,8,9);
        integers = integers.stream().skip(2).limit(5).collect(Collectors.toList());
        System.out.println(integers);

        //unordered返回无序的流,有待验证

        /**
         *  1.串行流和并行流在功能的使用上是没差别的,唯一的差别就是单线程和多线程的执行
         *    建议是如果任务太小或者运行程序的机器是单核的话，就用串行流，如果任务比较大且运行程序的机器是多核，就可以考虑用并行流
         *
         *  2.stream创建串行流,parallelStream创建并行流
         *  3.parallel将串行流转化为并行流,sequential将并行流转化为串行流
         *
         */
        //stream创建串行流
        LocalDateTime start = LocalDateTime.now();
        Random r = new Random();
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 10000; i ++){
            ints.add(r.nextInt());
        }
        ints = ints.stream().sorted().collect(Collectors.toList());
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        System.out.println(duration.toMillis() + "ms");

        //parallelStream创建并行流
        start = LocalDateTime.now();
        ints = new ArrayList<>();
        for (int i = 0; i < 10000; i ++){
            ints.add(r.nextInt());
        }
        ints = ints.parallelStream().sorted().collect(Collectors.toList());
        end = LocalDateTime.now();
        duration = Duration.between(start, end);
        System.out.println(duration.toMillis() + "ms");

        //parallel将串行流转化为并行流,sequential将并行流转化为串行流
        Stream<Integer> s = ints.stream().parallel();
        s = s.sequential();

        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);

        //会报错,一个流只能有一个terminal操作
//        Stream<String> ss = strings.stream();
//        ss.forEach(System.out::println);
//        ss.forEach(System.out::println);

        //count,min,max的用法
        strings = Arrays.asList("abce", "bc", "", "efg", "abcd", "jkl");
        long count = strings.stream().filter(String::isEmpty).count();
        System.out.println(count);
        Optional<String> optional = strings.stream().min(Comparator.comparing(String::length));
        optional.ifPresent(System.out::println);
        optional = strings.stream().max(Comparator.comparing(String::length));
        optional.ifPresent(System.out::println);

        //toArray的用法
        Object[] objects = strings.stream().skip(1).limit(3).toArray();
        System.out.println(Arrays.toString(objects));

        //forEachOrdered有待验证

        /**
         * allMatch,anyMatch,noneMatch的用法
         *  1.allMatch: 只有在所有的元素都满足断言时才返回true,否则flase,流为空时总是返回true
         *  2.anyMatch: 只有在任意一个元素满足断言时就返回true,否则flase
         *  3.noneMatch: 只有在所有的元素都不满足断言时才返回true,否则flase
         */
        System.out.println(Stream.empty().allMatch(i -> false));            //true
        System.out.println(Stream.of(1,2,3,4,5).allMatch(i -> i > 2));      //false
        System.out.println(Stream.of(1,2,3,4,5).allMatch(i -> i > 0));      //true
        System.out.println(Stream.of(1,2,3,4,5).anyMatch(i -> i > 2));      //true
        System.out.println(Stream.of(1,2,3,4,5).anyMatch(i -> i < 0));      //false
        System.out.println(Stream.of(1,2,3,4,5).noneMatch(i -> i > 6));     //true
        System.out.println(Stream.of(1,2,3,4,5).noneMatch(i -> i > 2));     //false

        /**
         * findFirst,findAny
         *  1.findFirst: 返回第一个元素，如果流为空，返回空的Optional
         *  2.findAny: 返回任意一个元素，如果流为空，返回空的Optional
         */
        Optional o = Stream.empty().findAny();
        System.out.println(o);
        o = Stream.of(2,3,4,1).findAny();
        System.out.println(o);
        o = Stream.empty().findFirst();
        System.out.println(o);
        o = Stream.of(1,2,3).findFirst();
        System.out.println(o);

        /**
         * reduce,collect暂不了解
         */
        Optional<Integer> op = Stream.of(1,2,3,4).reduce((x,y) -> x-y); //1-2-3-4
        op.ifPresent(System.out::println);
        Integer sum = Stream.of(1,2,3,4,5).reduce(10,(x,y) -> x-y);//10-1-2-3-4-5
        System.out.println(sum);

        System.exit(0);
    }

    @Data
    class C{

        private String name;
        private Integer age;
        private String gender;

        C(String name, Integer age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
    }



    /**
     * java8新特性: Lambda表达式,前面介绍的差不多了
     */
    public void lambda(){

    }
}
