package com.mouensis.server.identity.entity.pk;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author zhuyuan
 * @date 2020/12/21 17:53
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedClientEntityPk implements Serializable {
    private static final long serialVersionUID = -3725660189893340312L;
    private String authorizedClientId;
    private String principalName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizedClientEntityPk that = (AuthorizedClientEntityPk) o;
        return Objects.equals(authorizedClientId, that.authorizedClientId) &&
                Objects.equals(principalName, that.principalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizedClientId, principalName);
    }
}
