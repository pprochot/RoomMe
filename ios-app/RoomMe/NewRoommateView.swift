//
//  NewRoommateView.swift
//  RoomMe
//
//  Created by Student2 on 02/06/2022.
//

import SwiftUI

struct NewRoommateView: View {
    @State var username = ""
    @Binding var tasks: [Task3]
    var body: some View {
        
        VStack
        {
            Text("Enter friends username")
            HStack{
            Image(systemName: "person.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("username", text: $username)
            .foregroundColor(.gray)
            .font(.headline)
            }
            Spacer()
            
            Button(action: {
                            
                           withAnimation(.easeOut(duration: 0.3)) {
                               var added = false
                               for friend in tasksGlobal {
                                   if(friend.description == username) {
                                        added = true
                                   }
                               }
                               
                               for friend in tasks {
                                   if(friend.description == username) {
                                        added = false
                                   }
                               }
                               
                               if(added) {
                                    tasks.append(Task3(description: username))
                               }
                               else {
                                   NSLog("nie udalo sie dodac najlepszego przyjaciela")
                               }
                           }
                       }) {
                           Text("ADD")
                       }
                       .padding()
                       .frame(width: 150, height: 100)
                        .cornerRadius(40)
        }
    }
}



