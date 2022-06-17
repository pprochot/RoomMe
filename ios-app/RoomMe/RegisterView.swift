//
//  RegisterView.swift
//  RoomMe
//
//  Created by Student2 on 29/03/2022.
//

import SwiftUI

struct RegisterView: View {
    @Binding var push: Int
    
    @State var email = ""
    @State var username = ""
    @State var password = ""
    @State var repeatPassword = ""
    @State var firstName = ""
    @State var lastName = ""
    @State var phoneNumber = ""
    
    var body: some View {
            Form {
                Section {
                    Text("Register")
                        .frame(maxWidth: .infinity, alignment: .center)
                        .font(.system(size: 27))

                    HStack {
                        Image(systemName: "envelope")
                            .foregroundColor(.gray)
                            .imageScale(.large)
                            TextField("Email", text: $email)
                            .foregroundColor(.gray)
                            .font(.headline)
                    }
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                    .padding()
                    
                
                    HStack {
                        Image(systemName: "person")
                            .foregroundColor(.gray)
                            .imageScale(.large)
                            TextField("Username", text: $username)
                            .foregroundColor(.gray)
                            .font(.headline)
                    }
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
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                    .padding()
                
                    HStack {
                        Image(systemName: "lock")
                            .foregroundColor(.gray)
                            .imageScale(.large)
                            SecureField("Repeat password", text: $repeatPassword)
                            .foregroundColor(.gray)
                            .font(.headline)
                    }
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                    .padding()
               
                    HStack {
                        Image(systemName: "person")
                            .foregroundColor(.gray)
                            .imageScale(.large)
                            TextField("First name", text: $firstName)
                            .foregroundColor(.gray)
                            .font(.headline)
                    }
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                    .padding()
                
                    HStack {
                        Image(systemName: "person")
                            .foregroundColor(.gray)
                            .imageScale(.large)
                            TextField("Last name", text: $lastName)
                            .foregroundColor(.gray)
                            .font(.headline)
                    }
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                    .padding()
               
                    HStack {
                        Image(systemName: "phone")
                            .foregroundColor(.gray)
                            .imageScale(.large)
                            TextField("Phone number", text: $phoneNumber)
                            .foregroundColor(.gray)
                            .font(.headline)
                    }
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                    .padding()
                
                
                HStack {
                    Spacer()
                Button(action: {
                               withAnimation(.easeOut(duration: 0.3)) {
                                   let body: [String: Any] = [
                                                               "nickname": "\(username)",
                                                               "email": "\(email)",
                                                               "password": "\(password)",
                                                               "firstname": "\(firstName)",
                                                               "lastname": "\(lastName)",
                                                               "phoneNumber": "\(phoneNumber)"
                                                            ]
                                   //let rs = RequestSender()
                                   let url = URL(string: "http://959a-95-155-99-184.ngrok.io/auth/sign-up")!
                                   var request = URLRequest(url: url)
                                   request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                                   request.setValue("application/json", forHTTPHeaderField: "Accept")
                                   request.httpBody = try? JSONSerialization.data(withJSONObject: body)
                                   request.httpMethod = "POST"
                                   let session = URLSession(configuration: .default)
                                   let task = session.dataTask(with: request) { data, response, error in
                                       guard let data = data else { return }
                                       var result = String(data: data, encoding: .utf8)!
                                       NSLog(result)
                                       if(result.contains("\"result\":true")) {
                                            self.push = 0
                                       }
                                       else {
                                           NSLog("logowanie nieudane")
                                       }
                                   }
                                   task.resume()
                               }
                           }) {
                               Text("Register")
                                   .padding()
                                   .background(Color.orange)
                                   .foregroundColor(Color.white)
                                   .cornerRadius(10)
                           }
                   Spacer()
                       }
                       .edgesIgnoringSafeArea(.all)
                    
                    
                 
            }
            }}
        
    }
