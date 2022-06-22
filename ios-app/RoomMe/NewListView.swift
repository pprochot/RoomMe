//
//  NewListView.swift
//  RoomMe
//
//  Created by Student2 on 02/06/2022.
//

import SwiftUI

struct NewListView: View {
    @State var listName = ""
    @State var exists = false
    @Binding var tasks: [Task2]
    var body: some View {
        
        VStack
        {
            Text("Enter list name")
            HStack{
            Image(systemName: "list.bullet")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("List name", text: $listName)
            .foregroundColor(.gray)
            .font(.headline)
            }
            Spacer()
            
            Button(action: {
                            
                           withAnimation(.easeOut(duration: 0.3)) {
                               exists = true
                               tasks.append(Task2(description: listName))
                               for task in tasks {
                                   NSLog(task.description)
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

