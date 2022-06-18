//
//  LoginView.swift
//  RoomMe
//
//  Created by Student2 on 05/04/2022.
//



import SwiftUI
var loginGlobal = "login global"
struct LoginView: View {
    @Binding var push: Int
    @State var login = ""
    @State var password = ""
    
  
    var body: some View {
            
        VStack{
            Spacer()
            
            HStack {
                Image(systemName: "person")
                    .foregroundColor(.gray)
                    .imageScale(.large)
                    TextField("Login", text: $login)
                    .foregroundColor(.gray)
                    .font(.headline)
            }
                .padding(.top, 30)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                            .padding()
            HStack {
                Image(systemName: "lock")
                    .foregroundColor(.gray)
                    .imageScale(.large)
                    SecureField("Password", text: $password)
                    .foregroundColor(.gray)
                    .font(.headline)
            }
                .padding(.top, 30)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                            .padding()
  
            HStack{  // register i forgot password
                
            
                Button(action: {
                           withAnimation(.easeOut(duration: 0.3)) {
                               self.push = 2
                           }
                       }) {
                           Text("Register")
                       }
                       .padding()
                       .frame(width: 100, height: 100)
                        .cornerRadius(40)
                
                        
                Spacer()
                
                Button(action: {
                               withAnimation(.easeOut(duration: 0.3)) {
                                   self.push = 3
                               }
                           }) {
                               Text("Forgot password")
                           }
                
                           .padding()
                           .frame(width: 150, height: 100)
                            .cornerRadius(40)
                
                
                 
            }
            Spacer()
                        Button(action: {
                           withAnimation(.easeOut(duration: 0.3)) {
                               loginGlobal = login
                               let url = URL(string: "http://959a-95-155-99-184.ngrok.io/auth/sign-in")!
                               var request = URLRequest(url: url)
                               request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                               request.setValue("application/json", forHTTPHeaderField: "Accept")
                               
                               let body: [String: Any] = ["email": "\(login)",
                                                          "password": "\(password)"
                                                        ]
                               request.httpBody = try? JSONSerialization.data(withJSONObject: body)
                               request.httpMethod = "POST"
                               let session = URLSession(configuration: .default)
                               let task = session.dataTask(with: request) { data, response, error in
                                   guard let data = data else { return }
                                   var result = String(data: data, encoding: .utf8)!
                                   NSLog(result)
                                   if(result.contains("\"result\":true")) {
                                        self.push = 1
                                   }
                                   else {
                                       NSLog("logowanie nieudane")
                                   }
                               }
                               task.resume()
                           }
                       }) {
                           Text("Login")
                               .padding()
                               .background(Color.green)
                                .foregroundColor(Color.white)
                                .cornerRadius(10)
                       }
             
            Spacer()
        }
        .edgesIgnoringSafeArea(.all)
    }
}

