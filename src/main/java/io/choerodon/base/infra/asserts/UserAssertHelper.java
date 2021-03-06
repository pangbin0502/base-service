package io.choerodon.base.infra.asserts;

import org.springframework.stereotype.Component;

import io.choerodon.base.infra.dto.UserDTO;
import io.choerodon.core.exception.ext.AlreadyExistedException;
import io.choerodon.base.infra.mapper.UserMapper;
import io.choerodon.core.exception.CommonException;

/**
 * 用户断言帮助类
 *
 * @author superlee
 * @since 2019-05-10
 */
@Component
public class UserAssertHelper extends AssertHelper {

    private UserMapper userMapper;

    public UserAssertHelper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void notExternalUser(Long originOrgId, Long userOrgId) {
        if (!originOrgId.equals(userOrgId)) {
            throw new CommonException("error.user.update.external", originOrgId, userOrgId);
        }
    }

    public UserDTO userNotExisted(Long id) {
        return userNotExisted(id, "error.user.not.exist");
    }

    public UserDTO userNotExisted(WhichColumn whichColumn, String value) {
        switch (whichColumn) {
            case LOGIN_NAME:
                return loginNameNotExisted(value, "error.user.loginName.not.existed");
            case EMAIL:
                return emailNotExisted(value, "error.user.email.not.existed");
            default:
                throw new CommonException("error.illegal.whichColumn", whichColumn.value);
        }
    }

    public UserDTO userNotExisted(WhichColumn whichColumn, String value, String message) {
        switch (whichColumn) {
            case LOGIN_NAME:
                return loginNameNotExisted(value, message);
            case EMAIL:
                return emailNotExisted(value, message);
            default:
                throw new CommonException("error.illegal.whichColumn", whichColumn.value);
        }

    }

    private UserDTO emailNotExisted(String email, String message) {
        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        UserDTO result = userMapper.selectOne(dto);
        if (result == null) {
            throw new CommonException(message, email);
        }
        return result;
    }

    private UserDTO loginNameNotExisted(String loginName, String message) {
        UserDTO dto = new UserDTO();
        dto.setLoginName(loginName);
        UserDTO result = userMapper.selectOne(dto);
        if (result == null) {
            throw new CommonException(message, loginName);
        }
        return result;
    }

    public UserDTO userNotExisted(Long id, String message) {
        UserDTO dto = userMapper.selectByPrimaryKey(id);
        if (dto == null) {
            throw new CommonException(message, id);
        }
        return dto;
    }

    public void loginNameExisted(String loginName) {
        loginNameExisted(loginName, "error.user.loginName.exist");
    }

    public void loginNameExisted(String loginName, String message) {
        UserDTO dto = new UserDTO();
        dto.setLoginName(loginName);
        if (userMapper.selectOne(dto) != null) {
            throw new AlreadyExistedException(message);
        }
    }

    public void emailExisted(String email) {
        emailExisted(email, "error.user.email.existed");
    }

    public void emailExisted(String email, String message) {
        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        if (userMapper.selectOne(dto) != null) {
            throw new AlreadyExistedException(message);
        }
    }

    public enum WhichColumn {

        /**
         * 登录名字段
         */
        LOGIN_NAME("login_name"),

        /**
         * 邮箱字段
         */
        EMAIL("email");

        private String value;

        WhichColumn(String value) {
            this.value = value;
        }

        public static boolean contains(String value) {
            for (WhichColumn whichColumn : WhichColumn.values()) {
                if (whichColumn.value.equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }
}
