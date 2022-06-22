//MenuView.swift

import SwiftUI

struct SideBarView: View {
    @Binding var sceneNumber: Int
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Button(action: {
                               withAnimation(.easeOut(duration: 0.3)) {
                                   sceneNumber = 0
                               }
                           }) {
                               Image(systemName: "house")
                                   .foregroundColor(.gray)
                                   .imageScale(.large)
                               Text("Home")
                                   .foregroundColor(.gray)
                                   .font(.headline)
                           }
            }
            .padding(.top, 100)
            
            HStack {
                Button(action: {
                               withAnimation(.easeOut(duration: 0.3)) {
                                   sceneNumber = 1
                               }
                }) {
                    Image(systemName: "person")
                    .foregroundColor(.gray)
                    .imageScale(.large)
                Text("Profile")
                    .foregroundColor(.gray)
                    .font(.headline)
                }
            }
                .padding(.top, 30)
            
            HStack {
                Button(action: {
                               withAnimation(.easeOut(duration: 0.3)) {
                                   sceneNumber = 2
                               }
                }) {
                Image(systemName: "person.2")
                    .foregroundColor(.gray)
                    .imageScale(.large)
                Text("Friends")
                    .foregroundColor(.gray)
                    .font(.headline)
                }
            }
            .padding(.top, 30)
            
            Spacer()
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color(red: 32/255, green: 32/255, blue: 32/255))
        .edgesIgnoringSafeArea(.all)
    }
}

