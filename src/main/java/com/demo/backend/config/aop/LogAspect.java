package com.demo.backend.config.aop;

import com.demo.backend.model.AdminUser;
import com.demo.backend.service.SiteService;
import com.demo.backend.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import static com.demo.backend.utils.IpUtil.getIpAddr;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Resource
    private SiteService siteService;

    @Pointcut("execution(public * com.demo.backend.controller.*.*(..))")
    public void aspect(){}


    /**
     * 切面方法,记录日志
     * @return
     * @throws Throwable
     */
    @Around("aspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //开始时间
        long beginTime = System.currentTimeMillis();

        //获取requst对象
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String uri = requestAttr.getRequest().getRequestURI();

        log.info("开始计时: {}  URI: {}", new Date(),uri);

        //访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();

        //方法名获取
        String methodName = joinPoint.getSignature().getName();
        log.info("请求方法：{}, 请求参数: {}", methodName, Arrays.toString(args));
        //可能在反向代理请求进来时，获取的IP存在不正确行
        log.info("请求ip：{}", getIpAddr(requestAttr.getRequest()));

        Signature signature = joinPoint.getSignature();
        if(!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("暂不支持非方法注解");
        }

        Object object = null;
        //调用实际方法
        object = joinPoint.proceed();
        //获取执行的方法
        MethodSignature methodSign = (MethodSignature) signature;
        Method method = methodSign.getMethod();
        //判断是否包含了 无需记录日志的方法
        Log logAnno = AnnotationUtils.getAnnotation(method, Log.class);
        if(logAnno != null && logAnno.ignore()) {
            return object;
        }
        log.info("log注解描述：{}", logAnno.desc());
        long endTime = System.currentTimeMillis();
        log.info("结束计时: {},  URI: {}, 耗时：{}", new Date(),uri,endTime - beginTime);


        //日志入库
        saveLog(joinPoint, beginTime);

        return object;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //记录请求日志
        com.demo.backend.model.Log log = new com.demo.backend.model.Log();

        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {

            // 描述
            log.setLogInfo(logAnnotation.value());

        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        log.setMethod(className + "." + methodName + "()");

        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
            }
            log.setParams(params);
        }

        HttpSession session = httpServletRequest.getSession();
        AdminUser adminUser = (AdminUser) session.getAttribute(Constants.SESSION_KEY);

        if(null == adminUser)
        {
            log.setUserId((byte)0);
            log.setUserName("NoName");
        }else
        {
            log.setUserId((byte)(adminUser.getUserId()));
            log.setUserName(adminUser.getUsername());
        }

        log.setIp(getIpAddr(httpServletRequest));
        log.setPubTime(time / 1000);

        siteService.saveLog(log);
    }

    @AfterThrowing(pointcut="aspect()",throwing="e")
    public void afterThrowable(Throwable e) {
        log.error("切面发生了异常：", e);

        //throw new AopException("xxx);
    }

}
