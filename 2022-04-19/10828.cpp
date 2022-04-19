#include <bits/stdc++.h>

using namespace std;

int main(){
    int n;
    cin >> n;
    stack<int> S;
    while(n--) {
        string s;
        cin >> s;
        if(s == "push") {
            int num;
            cin >> num;
            S.push(num);
        }
        else if(s == "pop") {
            if(S.empty()) cout << -1 << "\n";
            else {
                cout << S.top() << "\n";
                S.pop();
            }
        }
        else if(s == "size"){ 
            cout << S.size() << "\n";
            
        }
        else if(s == "empty"){
            cout << S.empty() << "\n";
        }
        else if(s == "top"){
            if(S.empty()) cout << -1 << "\n";
            else {
                cout << S.top() << "\n";
            }
        }
    }
}