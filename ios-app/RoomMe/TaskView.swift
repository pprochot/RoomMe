//
//  TaskView.swift
//  RoomMe
//
//  Created by Student2 on 18/05/2022.
//

import SwiftUI

struct Task: Identifiable {
    var id = UUID()
    var description: String
}

struct TaskRow: View {
    var task: Task
    var body: some View {
        HStack{
            Image(systemName: "largecircle.fill.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
                Text("\(task.description)")
            .foregroundColor(.gray)
            .font(.headline)
        }
    }
}

var first = Task(description: "grzybobranie")
var second = Task(description: "rosol cooking")
//var tasks = [first, second]
struct TaskView: View {
    @Binding var sceneNumber: Int
    @State var tasks = [first, second]
    var body: some View {
        VStack{
            HStack {
            List(tasks) {
                task in TaskRow(task: task)
            }}
        NavigationView {
            
            VStack{
                Spacer()
                HStack {
                    
                    NavigationLink(destination: NewHouseworkView(tasks: $tasks), label: {
                Text("CREATE NEW HOUSEWORK")
                    .padding()
                    .background(Color.green)
                     .foregroundColor(Color.white)
                     .cornerRadius(10)
            }
            )
                }
            }
            }
        }
    }
}

