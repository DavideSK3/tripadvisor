/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gabriele
 */
public final class StringDistanceUtil {
    
    
    public static String deleteAt(String s, int i){
        if(i < 0 || i >= s.length()) return s;
        else return s.substring(0, i) + s.substring(i+1);
    }
    
    public static String deleteAt(String s, int i, int j){
        if(i<0 || i>= s.length()) return deleteAt(s, j);
        else if(j < 0 || j>= s.length()) return deleteAt(s, i);
        else if( i == j) return deleteAt(s, i);
        else{
            int m = Math.min(i, j);
            int M = Math.max(i, j);
            
            return s.substring(0, m) + s.substring(m+1, M) + s.substring(M+1);
            
        }
    }
    
    public static Set<String> generateNearTerms(String term){
        HashSet<String> terms = new HashSet((int)(0.5*term.length()*(term.length()-1)) + term.length() + 1);
        terms.add(term);
        for(int i=0; i<term.length(); i++){
            terms.add(deleteAt(term, i));
        }
        
        for(int i=0; i<term.length()-1; i++){
            for(int j=i+1; j<term.length(); j++){
                terms.add(deleteAt(term, i, j));
            }
        }
        
        return terms;
    }
    
    public static int editDistance(String s1, String s2){
        int n = s1.length();
        int m = s2.length();
        int d[][] = new int[n+1][m+1];
        for(int i=0; i<=n; i++){
            d[i][0] = i;
        }
        for(int i=1; i<=m; i++){
            d[0][i] = i;
        }
        for(int i=1; i<=n; i++){
            for(int j=1; j<=m; j++){
                d[i][j] = Math.min(d[i-1][j-1] + (s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1), 1 + Math.min(d[i-1][j], d[i][j-1]));
            }
        }
        return d[n][m];
    }
    
    
    public static int editDistanceLimited(String s1, String s2, int k){
        int n = s1.length();
        int m = s2.length();
        k--;
        if(m > n){
            int t = n;
            n = m;
            m = t;
            String temp = s2;
            s2 = s1;
            s1 = temp;
        }
        
        
        int st, end;
        int st_1, end_1;
        
        int d[] = new int[2*k+3];
        int d_1[] = new int[2*k+3];
        
        st_1 = st = Math.max(0, m - n - k);
        end_1 = end = Math.min(m, m- n + k);
        
        
        
       
        
        
        for(int i=st_1; i<=end_1; i++){
            d_1[i -st_1 + 1] = d[i -st + 1] = i;
            
        }
        d_1[0] = d_1[2*k+2] = d[0] = d[2*k+2] = 0;
        
        
        for(int i=1; i<= n; i++){
            
            st = Math.max(0, m -n + i - k);
            end = Math.min(m, m -n + i + k);
            
            if(st == 0){
                d[1] = i;
            }else{
                d[1] = Math.min(d_1[1] + (s1.charAt(i-1) == s2.charAt(st-1) ? 0 : 1), 1);
            }
            
            for(int j=st+1; j<=end; j++){
                d[j-st + 1] = Math.min(d_1[j-1 -st_1 + 1] + (s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1), 1 + Math.min(d_1[j-st_1 + 1], d[j-1-st + 1]));
            }
            
            
            st_1 = st;
            end_1 = end;
            for(int j=0; j<=end-st; j++){
                d_1[j+1] = d[j+1];
            }
            
        }
        
        return d[end-st+1];
    }
    
    
}
