package com.eddywijaya.recruitmentbcaf.service;


import com.eddywijaya.recruitmentbcaf.dto.response.RespAccountDTO;
import com.eddywijaya.recruitmentbcaf.dto.response.RespMenuDTO;
import com.eddywijaya.recruitmentbcaf.dto.validasi.ValLoginDTO;
import com.eddywijaya.recruitmentbcaf.model.Account;
import com.eddywijaya.recruitmentbcaf.model.Menu;
import com.eddywijaya.recruitmentbcaf.repo.AccountRepo;
import com.eddywijaya.recruitmentbcaf.security.BcryptImpl;
import com.eddywijaya.recruitmentbcaf.security.Crypto;
import com.eddywijaya.recruitmentbcaf.security.JwtUtility;
import com.eddywijaya.recruitmentbcaf.util.GlobalFunction;
import com.eddywijaya.recruitmentbcaf.repo.AccountRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AppUserDetailService implements UserDetailsService {


    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private JwtUtility jwtUtility;

    private ModelMapper modelMapper = new ModelMapper();
    public ResponseEntity<Object> doLogin(Account account , HttpServletRequest request){

        Optional<Account> optionalAccount = accountRepo.findByUsername(account.getUsername());
        if(!optionalAccount.isPresent()){
            return GlobalFunction.dataTidakDitemukan(request);
        }

        Account nextAccount = optionalAccount.get();
        if(!BcryptImpl.verifyHash((account.getPassword()+account.getUsername()),nextAccount.getPassword())){
            return GlobalFunction.dataTidakDitemukan(request);
        }

        UserDetails accountDetails = loadUserByUsername(account.getUsername());
        /** start jwt config */
        Map<String,Object> mapForClaims = new HashMap<>();
        mapForClaims.put("uid",nextAccount.getId());//payload
        mapForClaims.put("ml",nextAccount.getEmail());//payload
        mapForClaims.put("fn",nextAccount.getFullName());//payload
        mapForClaims.put("pw",nextAccount.getPassword());//payload
        String token = jwtUtility.generateToken(accountDetails,mapForClaims);
        Map<String,Object> m = new HashMap<>();
        m.put("token", Crypto.performEncrypt(token));
        /** end jwt config */
        m.put("menu", convertToListRespMenuDTO(nextAccount.getAccess().getMenus()));
//        m.put("menu", null);
//        if (nextAccount.getAccess() != null && nextAccount.getAccess().getMenus() != null) {
//            m.put("menu", convertToListRespMenuDTO(nextAccount.getAccess().getMenus()));
//        } else {
//            m.put("menu", null);
//        }
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Account> opAccount = accountRepo.findByUsername(s);
        if(opAccount.isEmpty())
        {
            throw new UsernameNotFoundException("TOKEN TIDAK VALID");
//            return null;
        }
        Account accountNext = opAccount.get();

        return new org.springframework.security.core.userdetails.
                User(
                        accountNext.getUsername(),
                        accountNext.getPassword(),
                        accountNext.getAuthorities()
                );
    }

    public Account convertToEntiy(ValLoginDTO valLoginDTO){
        return modelMapper.map(valLoginDTO, Account.class);
    }

    public List<RespMenuDTO> convertToListRespMenuDTO(List<Menu> menus){
        return modelMapper.map(menus,new TypeToken<List<RespMenuDTO>>(){}.getType());
    }

}
