package com.adminview.ims.service.security;

import java.util.Map;

public interface ILoginService {

    public Map<String, Object> login(Map<String, Object> parm);

    public void updatePassword(Map<String, Object> parm);

    public void resetPassword(Map<String, Object> parm);

}
