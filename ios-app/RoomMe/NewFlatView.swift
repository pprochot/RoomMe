//
//  NewFlatView.swift
//  RoomMe
//
//  Created by Student2 on 01/06/2022.
//

import SwiftUI

struct NewFlatView: View {
    @State var flatName = ""
    @State var address = ""
    @State var exists = false
    @Binding var tasks: [Task1]
    var body: some View {
        
        VStack
        {
            Text("Enter flat name")
            HStack{
            Image(systemName: "house")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("Flat name", text: $flatName)
            .foregroundColor(.gray)
            .font(.headline)
            }
            Spacer()
            
            Text("Enter address")
            HStack{
            Image(systemName: "location.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("Address", text: $address)
            .foregroundColor(.gray)
            .font(.headline)
            }
            
            Button(action: {
                            
                           withAnimation(.easeOut(duration: 0.3)) {
                               exists = true
                               tasks.append(Task1(description: flatName, address: address))
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

