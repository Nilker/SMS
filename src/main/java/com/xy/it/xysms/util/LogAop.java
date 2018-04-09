package com.xy.it.xysms.util;



import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * 时间: 2017-5-4
 */

@Aspect //  声明切面
@Component  //声明组件
public class LogAop {
//
//    /**
//     *自定义日志
//     */
//    private Logger logger = LoggerFactory.getLogger(LogAop.class);
//
//    ThreadLocal<Long> startTime = new ThreadLocal<>();
//
//    /**
//     * 打印类method的名称以及参数
//     *
//     * @param point 切面
//     */
//    public void printMethodParams(JoinPoint point) {
//        if (point == null) {
//            return;
//        }
//        /**
//         * Signature 包含了方法名、申明类型以及地址等信息
//         */
//        String class_name = point.getTarget().getClass().getName();
//        String method_name = point.getSignature().getName();
//        //重新定义日志
//        logger = LoggerFactory.getLogger(point.getTarget().getClass());
//        logger.info("class_name = {}", class_name);
//        logger.info("method_name = {}", method_name);
//
//        /**
//         * 获取方法的参数值数组。
//         */
//        Object[] method_args = point.getArgs();
//
//        try {
//            /**
//             * 获取方法参数名称
//             */
//            String[] paramNames = getFieldsName(class_name, method_name);
//
//            /**
//             * 打印方法的参数名和参数值
//             */
//            logParam(paramNames, method_args);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 使用javassist来获取方法参数名称
//     *
//     * @param class_name  类名
//     * @param method_name 方法名
//     * @return
//     * @throws Exception
//     */
//    private String[] getFieldsName(String class_name, String method_name) throws Exception {
//        Class<?> clazz = Class.forName(class_name);
//        String clazz_name = clazz.getName();
//        ClassPool pool = ClassPool.getDefault();
//        ClassClassPath classPath = new ClassClassPath(clazz);
//        pool.insertClassPath(classPath);
//
//        CtClass ctClass = pool.get(clazz_name);
//        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
//        MethodInfo methodInfo = ctMethod.getMethodInfo();
//        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
//        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
//        if (attr == null) {
//            return null;
//        }
//        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
//        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
//        for (int i = 0; i < paramsArgsName.length; i++) {
//            paramsArgsName[i] = attr.variableName(i + pos);
//        }
//        return paramsArgsName;
//    }
//
//
//    /**
//     * 判断是否为基本类型：包括String
//     *
//     * @param clazz clazz
//     * @return true：是;     false：不是
//     */
//    private boolean isPrimite(Class<?> clazz) {
//        if (clazz.isPrimitive() || clazz == String.class) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    /**
//     * 打印方法参数值  基本类型直接打印，非基本类型需要重写toString方法
//     *
//     * @param paramsArgsName  方法参数名数组
//     * @param paramsArgsValue 方法参数值数组
//     */
//    private void logParam(String[] paramsArgsName, Object[] paramsArgsValue) {
//        if (ArrayUtils.isEmpty(paramsArgsName) || ArrayUtils.isEmpty(paramsArgsValue)) {
//            logger.info("该方法没有参数");
//            return;
//        }
//        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < paramsArgsName.length; i++) {
//            //参数名
//            String name = paramsArgsName[i];
//            //参数值
//            Object value = paramsArgsValue[i];
//            buffer.append(name + " = ");
//            if (isPrimite(value.getClass())) {
//                buffer.append(value + "  ,");
//            } else {
//                buffer.append(value.toString() + "  ,");
//            }
//        }
//        logger.info(buffer.toString());
//    }
//
//    /**
//     * <li>Before       : 在方法执行前进行切面</li>
//     * <li>execution    : 定义切面表达式</li>
//     * <p>public * com.eparty.ccp.*.impl..*.*(..)
//     *      <li>public :匹配所有目标类的public方法，不写则匹配所有访问权限</li>
//     *      <li>第一个* :方法返回值类型，*代表所有类型 </li>
//     *      <li>第二个* :包路径的通配符</li>
//     *      <li>第三个..* :表示impl这个目录下所有的类，包括子目录的类</li>
//     *      <li>第四个*(..) : *表示所有任意方法名,..表示任意参数</li>
//     * </p>
//     * @param point 切面
//     */
//    //@Before("execution(public * com.eparty.ccp.*.impl..*.*(..))")
//    //excution(<修饰符模式> ? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
//            /**
//             * 匹配 com.aop.learn.service 包下所有类的所有方法
//             */
//     //@Before("execution(* com.aop.learn.service.*(..))")
//    @Before("execution(* com.xy.it.xysms.controller.XySmsController.*(..))")
//    public void before(JoinPoint point) {
//        logger.info(">>>>>>>>>>>>>>>>>>>>>>>方法开始>>>>>>>>>>>>>>>>>>>>>>>");
//        this.printMethodParams(point);
//        startTime.set(System.currentTimeMillis());
//    }
//
//
//    @AfterReturning(returning = "ret", pointcut = "execution(* com.xy.it.xysms.controller.XySmsController.*(..))")
//    public void doAfterReturning(Object ret) throws Throwable {
//        // 处理完请求，返回内容
//        logger.info("RESPONSE : " + ret);
//        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
//        logger.info("<<<<<<<<<<<<<<<<<<<<<<<方法结束<<<<<<<<<<<<<<<<<<<<<<<");
//    }


    private static Logger log = LoggerFactory.getLogger(LogAop.class);

    private Gson gson = new Gson();

    ThreadLocal<Long>  startTime = new ThreadLocal<Long>();


    //申明一个切点 里面是 execution表达式
    @Pointcut("execution(* com.xy.it.xysms.controller.XySmsController.*(..))")
    private void controllerAspect() {
    }


    //请求method前打印内容
    @Before(value = "controllerAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //打印请求内容
        log.info("===============请求内容===============");
        log.info("请求地址:" + request.getRequestURL().toString());
        log.info("请求方式:" + request.getMethod());
        log.info("请求类方法:" + joinPoint.getSignature());
        log.info("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));
        log.info("===============请求内容===============");
    }


    //在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "controllerAspect()")
    public void methodAfterReturing(Object o) {
        log.info("--------------返回内容----------------");
        log.info("Response内容:" + gson.toJson(o));
        log.info("--------------返回内容----------------");

        log.info("请求处理时间为:"+(System.currentTimeMillis() - startTime.get()));

    }
}
