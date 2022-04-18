#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    int t;
    cin >> t;
    while(t--){
        string init;
        cin >> init;
        list<char> k;
        auto cursor = k.begin();
        for(auto t : init){
            
            if(t == '<'){
                if(cursor != k.begin()) cursor--;
            }
            else if(t == '>') {
                if(cursor != k.end()) cursor++;
            }
            else if(t == '-'){
                if(cursor != k.begin()){
                    cursor--;
                    cursor = k.erase(cursor);
                }
            }
            else {
                k.insert(cursor, t);
            }
        }
        for(auto a: k) {
            cout << a;
        }
        cout << "\n";
        
    }
    
    
}