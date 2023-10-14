package org.cloud.xue.redis.springJedis;

import org.cloud.xue.common.bean.User;

public interface UserService {

    /**
     * CRUD-查询
     * @param id
     * @return
     */
    User getUser(long id);

    /**
     * CRUD-新增/更新
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * CRUD - 删除
     * @param id
     */
    void deleteUser(long id);

    /**
     * 删除全部信息
     */
    public void deleteAll();
}
