//
//  TaskView.swift
//  RoomMe
//
//  Created by Student2 on 31/05/2022.
//

import SwiftUI

struct NewFrienfView: View {
    @State var listName = ""
    @Binding var tasks: [BestFriend]
    var body: some View {
        
        VStack
        {
            Text("Enter friends name")
            HStack{
            Image(systemName: "person.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("Friends name", text: $listName)
            .foregroundColor(.gray)
            .font(.headline)
            }
            Spacer()
            
            Button(action: {
                            
                           withAnimation(.easeOut(duration: 0.3)) {
                               tasks.append(BestFriend(description: listName))
                               tasksGlobal.append(BestFriend(description: listName))
                           }
                       }) {
                           Text("ADD")
                       }
                       .padding()
                       .frame(width: 150, height: 100)
                        .cornerRadius(40)
            //print(exists)
        }
    }
}


