#include <bits/stdc++.h>

using namespace std;

int pre[10005];
int nxt[10005];
int main(void) {
    
    int count = 1;
    int n, k;
    cin >> n >> k;
    for(int i=1; i<=n; i++){
        if(i == 1){
            pre[i] = n;
            nxt[i] = i+1;
        }
        else if(i == n) {
            pre[i] = n-1;
            nxt[i] = 1;
            
        }else {
            pre[i] = i-1;
            nxt[i] = i+1;
        }
    }
    
    vector <int> v;
    for(int i=1; n>0; i=nxt[i]) {
        if(count == k) {
            v.push_back(i);
            pre[nxt[i]] = pre[i];
            nxt[pre[i]] = nxt[i];
            
            count = 0;
            n--;
        }
        count++;
    }
    cout << "<";
    for(int i=0; i<v.size(); i++){
        cout << v[i];
        if(i != v.size()-1) cout <<", ";
    }
    cout << ">";
    
}