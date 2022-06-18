//
//  NewFlatView.swift
//  RoomMe
//
//  Created by Student2 on 01/06/2022.
//

import SwiftUI

struct AddListItemsView: View {
    @State var item = ""
    @State var exists = false
    @Binding var tasks: [Item]
    var body: some View {
        
        VStack
        {
            Text("Enter item")
            HStack{
            Image(systemName: "cart.badge.plus")
                .foregroundColor(.gray)
                .imageScale(.large)
            TextField("Item", text: $item)
            .foregroundColor(.gray)
            .font(.headline)
            }
            Spacer()
            
            
            Button(action: {
                            
                           withAnimation(.easeOut(duration: 0.3)) {
                               exists = true
                               tasks.append(Item(description: item))
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
            //print(exists)
        }
    }
    
}

