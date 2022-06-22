//
//  NewHouseworkView.swift
//  RoomMe
//
//  Created by Student2 on 02/06/2022.
//

import SwiftUI

struct NewHouseworkView: View {
    @State var housework = ""
    @State var exists = false
    @Binding var tasks: [Task]
    var body: some View {
        
        VStack
        {
            Text("Enter housework")
            HStack{
            Image(systemName: "largecircle.fill.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("Housework", text: $housework)
            .foregroundColor(.gray)
            .font(.headline)
            }
            Spacer()
            
            Button(action: {
                            
                           withAnimation(.easeOut(duration: 0.3)) {
                               exists = true
                               tasks.append(Task(description: housework))
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
