#include <bits/stdc++.h>

using namespace std;

int main() {
    int K;
    cin >> K;
    stack <int> s;
    while(K--) {
        int num;
        cin >> num;
        if(num == 0){
            s.pop();
        }
        else s.push(num);
    }
    int sum = 0;
    while(!s.empty()){
        sum+=s.top();
        s.pop();
    }
    cout << sum;
}