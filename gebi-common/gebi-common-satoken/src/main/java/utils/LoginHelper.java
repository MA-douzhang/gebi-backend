package utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.madou.user.api.model.vo.LoginUserVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ltyzzz
 * @email ltyzzz2000@gmail.com
 * @date 2023/3/5 17:30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";

    public static void login(LoginUserVO loginUser) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getId());
        setLoginUser(loginUser);
    }

    public static void setLoginUser(LoginUserVO loginUser) {
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    public static LoginUserVO getLoginUser() {
        LoginUserVO loginUser = (LoginUserVO) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUserVO) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    public static Long getUserId() {
        LoginUserVO loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String[] strs = StringUtils.split(loginId, JOIN_CODE);
            return Long.parseLong(strs[strs.length - 1]);
        }
        return loginUser.getId();
    }

    public static Boolean isLogin() {
        return StpUtil.isLogin();
    }
}
