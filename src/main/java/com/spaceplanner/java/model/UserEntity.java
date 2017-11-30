package com.spaceplanner.java.model;

import com.spaceplanner.java.model.master.Role;
import com.spaceplanner.java.model.type.PasswordType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.security.Principal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 21/8/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name = "user")
public class UserEntity extends BaseEntity implements UserDetails, Principal {

    private static final long serialVersionUID = 3256446889040622647L;

    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    /*private Address address;*/
    private Set<Role> roles;
    private PasswordType passwordType = PasswordType.PERMANENT;
    private boolean accountNonExpired=true;
    private boolean accountNonLocked=true;
    private boolean credentialsNonExpired=true;
    private boolean enabled=true;
    private Set<String> roleName = new HashSet<String>();


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /*@Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }*/


    @ManyToMany(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SELECT)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        if(null==getRoles())
            setRoles(new HashSet<Role>());
        getRoles().add(role);
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "password_type")
    public PasswordType getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(PasswordType passwordType) {
        this.passwordType = passwordType;
    }

    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
            AUTHORITIES.add(new SimpleGrantedAuthority(role.getName()));
        }
        //AUTHORITIES.add(new SimpleGrantedAuthority(Constants.ROLE_NAME_ADMIN));
        return AUTHORITIES;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @javax.persistence.Column(name = "username", nullable = false, insertable = true, updatable = false, length = 255, precision = 0)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Transient
    public String getName(){
        return getFirstName()+" " +getLastName();
    }

    @Transient
    public boolean isUserInRole(String... roleNames){
        boolean isValid = false;
        if(null!=getRoles()){
            for(Role role :getRoles()){
                for(String roleName : roleNames){
                    if(roleName.equals(role.getName())){
                        isValid=true;
                        break;
                    }
                }
                if(isValid)
                    break;
            }
        }
        return isValid;
    }

    @Transient
    public Set<String> getRoleName() {
        if(null!= getRoles()){
            for(Role role : getRoles()){
                roleName.add(role.getName());
            }
        }
        return roleName;
    }

    public void setRoleName(Set<String> roleName) {
        this.roleName = roleName;
    }
    

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity userEntity = (UserEntity) o;

        if (id != null ? !id.equals(userEntity.id) : userEntity.id != null) return false;
        if (username != null ? !username.equals(userEntity.username) : userEntity.username != null) return false;

        return true;
    }


    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    public void trim() {
        //To change body of created methods use File | Settings | File Templates.
    }
}
