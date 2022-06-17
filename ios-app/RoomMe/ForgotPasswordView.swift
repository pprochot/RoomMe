//
//  ForgotPasswordView.swift
//  RoomMe
//
//  Created by Student2 on 29/03/2022.
//

import SwiftUI

struct ForgotPasswordView: View {
    @Binding var push: Int
    @State var email = ""
    var body: some View {
       
        VStack{
            Spacer()
                    VStack {
                    Text("Please enter your email.")
                    Text("Verification link will be sent there.")
                
                        HStack {
                    Image(systemName: "envelope")
                        .foregroundColor(.gray)
                        .imageScale(.large)
                        TextField("email", text: $email)
                        .foregroundColor(.gray)
                        .font(.headline)
                }
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()
                }
            Spacer()
            VStack{
            Button(action: {
                           withAnimation(.easeOut(duration: 0.3)) {
                               self.push = 0
                           }
                       }) {
                           Text("Send verification link")
                               .padding()
                               .background(Color.green)
                                .foregroundColor(Color.white)
                                .cornerRadius(10)
                       }
            
                }
            Spacer()
        }
}
}


